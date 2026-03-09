package com.jordyma.blink.feed_summarizer.service

import com.jordyma.blink.fcm.service.FcmService
import com.jordyma.blink.feed.domain.Source
import com.jordyma.blink.feed.domain.Status
import com.jordyma.blink.feed.domain.FeedRepository
import com.jordyma.blink.feed.service.FeedService
import com.jordyma.blink.feed_summarizer.html_parser.HtmlParser
import com.jordyma.blink.feed_summarizer.html_parser.HtmlParserV2
import com.jordyma.blink.feed_summarizer.listener.dto.FeedSummarizeMessage
import com.jordyma.blink.feed_summarizer.request_limiter.SummarizeRequestLimiter
import com.jordyma.blink.folder.service.FolderService
import com.jordyma.blink.gemini.GeminiService
import com.jordyma.blink.global.error.exception.BadRequestException
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.gemini.response.PromptResponse
import com.jordyma.blink.logger
import com.jordyma.blink.user.UserRepository
import kotlinx.coroutines.*
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
@Primary
class RedissonFeedSummarizerService(
    private val redissonClient: RedissonClient,
    private val summarizeRequestLimiter: SummarizeRequestLimiter,
    private val htmlParser: HtmlParser,
    private val htmlParserV2: HtmlParserV2,
    private val folderService: FolderService,
    private val geminiService: GeminiService,
    private val feedService: FeedService,
    private val feedRepository: FeedRepository,
    private val userRepository: UserRepository,
    private val fcmService: FcmService,
): FeedSummarizerService {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun summarizeFeed(payload: FeedSummarizeMessage): PromptResponse? {
        runBlocking {
            summarizeFeedAsync(payload)
        }
        return null
    }

    private suspend fun summarizeFeedAsync(payload: FeedSummarizeMessage) = coroutineScope {
        val userId = payload.userId
        val link = payload.link
        val feedId = payload.feedId.toLong()
        
        val lockKey = "blink:processing:$userId:$feedId"
        val lock = redissonClient.getLock(lockKey)
        
        // 1. 비동기 락 획득 
        val acquired = withContext(Dispatchers.IO) {
            try {
                lock.tryLock(0, -1, TimeUnit.SECONDS)
            } catch (e: InterruptedException) {
                log.warn("Lock interrupted: feedId=$feedId", e)
                false
            }
        }
        
        if (!acquired) {
            log.info("Already processing, skipping: feedId=$feedId, userId=$userId")
            return@coroutineScope
        }
        
        try {
            // 2분 타임아웃 
            withTimeout(120_000) {
                
                // 2. HTML 파싱 
                log.info("Starting HTML parsing: feedId=$feedId, url=$link")
                val parseStart = System.currentTimeMillis()
                
                val parseContent = withContext(Dispatchers.IO) {
                    htmlParserV2.parseUrl(link)
                }
                
                val parseDuration = System.currentTimeMillis() - parseStart
                log.info("HTML parsing completed: feedId=$feedId, duration=${parseDuration}ms, size=${parseContent.content.length}")
                
                var thumbnailImage = parseContent.thumbnailImage
                
                // 3. Gemini 요약 
                log.info("Starting Gemini summarization: feedId=$feedId")
                val summarizeStart = System.currentTimeMillis()
                
                val folderNames: List<String> = withContext(Dispatchers.IO) {
                    folderService.getFolders(userId = userId).map { it.name }
                }
                
                val content = withContext(Dispatchers.IO) {
                    geminiService.getContents(
                        link = link,
                        folders = folderNames.joinToString(separator = " "),
                        userId = userId,
                        parseContent.content,
                        feedId
                    )
                }
                
                if (content == null) {
                    throw ApplicationException(ErrorCode.JSON_PARSING_FAILED, "gemini exception: no content")
                }
                
                val summarizeDuration = System.currentTimeMillis() - summarizeStart
                log.info("Gemini summarization completed: feedId=$feedId, duration=${summarizeDuration}ms")
                
                // 4. 플랫폼별 이미지 처리
                val brunch = feedService.findBrunch(link)
                if (brunch == Source.BRUNCH) {
                    thumbnailImage = thumbnailImage.removePrefix("//")
                }
                
                // 5. DB 저장
                log.info("Saving summarized feed: feedId=$feedId")
                val feed = withContext(Dispatchers.IO) {
                    feedService.updateSummarizedFeed(
                        content.subject,
                        content.summary,
                        content.category,
                        content.keyword,
                        brunch,
                        feedId,
                        userId,
                        thumbnailImage,
                    )
                }
                
                // 6. 푸시 알림 
                launch(Dispatchers.IO) {
                    try {
                        val user = userRepository.findById(userId)
                            .orElseThrow { BadRequestException(com.jordyma.blink.global.error.ErrorCode("M1", "해당 사용자를 찾을 수 없습니다")) }
                        
                        if (user.iosPushToken != null || user.aosPushToken != null) {
                            fcmService.sendSummarizedAlert(userId, feed)
                            log.info("Push notification sent: feedId=$feedId, userId=$userId")
                        }
                    } catch (e: Exception) {
                        log.error("Failed to send push notification: feedId=$feedId", e)
                    }
                }
                
                log.info("Summarization completed successfully: feedId=$feedId, totalDuration=${parseDuration + summarizeDuration}ms")
            }
            
        } catch (e: TimeoutCancellationException) {
            log.error("Summarization timeout (>2min): feedId=$feedId, url=$link")
            withContext(Dispatchers.IO) {
                val feed = feedService.findFeedOrElseThrow(feedId)
                feed.updateStatus(Status.FAILED)
                feedRepository.save(feed)
            }
            
        } catch (e: CancellationException) {
            log.warn("Summarization cancelled: feedId=$feedId, url=$link")
            throw e  
            
        } catch (e: Exception) {
            log.error("Summarization failed: feedId=$feedId, url=${payload.originUrl}, userName=${payload.userName}", e)
            withContext(Dispatchers.IO) {
                val feed = feedService.findFeedOrElseThrow(feedId)
                feed.updateStatus(Status.FAILED)
                feedRepository.save(feed)
            }
            
        } finally {
            // 7. 락 해제 
            withContext(Dispatchers.IO) {
                try {
                    if (lock.isHeldByCurrentThread) {
                        lock.unlock()
                        log.debug("Lock released: feedId=$feedId")
                    }
                } catch (e: Exception) {
                    log.error("Failed to release lock: feedId=$feedId", e)
                }
            }
        }
    }

    override fun refillToken() {
        this.summarizeRequestLimiter.refillToken()
        log.info("Token refilled")
    }
}
