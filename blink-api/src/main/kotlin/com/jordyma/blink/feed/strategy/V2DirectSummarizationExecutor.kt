package com.jordyma.blink.feed.strategy

import com.jordyma.blink.fcm.service.FcmService
import com.jordyma.blink.feed.domain.FeedRepository
import com.jordyma.blink.feed.domain.Status
import com.jordyma.blink.feed.domain.model.FeedSummarizeMessage
import com.jordyma.blink.feed.domain.service.PageParser
import com.jordyma.blink.feed.service.FeedService
import com.jordyma.blink.folder.domain.service.FolderService
import com.jordyma.blink.global.coroutine.withMDCContext
import com.jordyma.blink.global.shutdown.ActiveRequestCounter
import com.jordyma.blink.infra.gemini.GeminiService
import com.jordyma.blink.user.UserRepository
import kotlinx.coroutines.*
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit


@Component
class V2DirectSummarizationExecutor(
    private val geminiService: GeminiService,
    private val htmlParser: PageParser,
    private val folderService: FolderService,
    private val feedService: FeedService,
    private val feedRepository: FeedRepository,
    private val fcmService: FcmService,
    private val userRepository: UserRepository,
    private val redissonClient: RedissonClient,
    private val activeRequestCounter: ActiveRequestCounter,
) : SummarizationExecutor {
    
    private val log = LoggerFactory.getLogger(this::class.java)

    override suspend fun execute(message: FeedSummarizeMessage): Long = coroutineScope {
        activeRequestCounter.increment()
        
        try {
            // 비동기로 던지고 즉시 반환
            launch(Dispatchers.Default + withMDCContext()) {
                summarizeAsync(message)
            }
            message.feedId
        } finally {
            activeRequestCounter.decrement()
        }
    }

    private suspend fun summarizeAsync(message: FeedSummarizeMessage) = coroutineScope {
        val lockKey = "blink:processing:${message.userId}:${message.feedId}"
        val lock = redissonClient.getLock(lockKey)
        
        MDC.put("feedId", message.feedId.toString())
        MDC.put("userId", message.userId.toString())
        MDC.put("strategy", "V2_DIRECT")
        MDC.put("operation", "summarize")
        
        val startTime = System.currentTimeMillis()
        
        try {
            // 1. 락 획득
            MDC.put("stage", "lock_acquire")
            val acquired = withContext(Dispatchers.IO + withMDCContext()) {
                try {
                    lock.tryLock(0, -1, TimeUnit.SECONDS)
                } catch (e: InterruptedException) {
                    log.warn("Lock interrupted: feedId=${message.feedId}", e)
                    false
                }
            }
            
            if (!acquired) {
                MDC.put("result", "skipped_duplicate")
                log.info("Already processing, skipping: feedId=${message.feedId}")
                return@coroutineScope
            }
            
            // 2. 타임아웃 설정 
            withTimeout(120_000) {
                // 3. HTML 파싱
                MDC.put("stage", "html_parsing")
                log.info("Starting HTML parsing: feedId=${message.feedId}, url=${message.link}")
                val parseStart = System.currentTimeMillis()
                
                val pageInfo = withContext(Dispatchers.IO + withMDCContext()) {
                    htmlParser.parseUrl(message.link)
                }
                
                val parseDuration = System.currentTimeMillis() - parseStart
                MDC.put("htmlParseDuration", parseDuration.toString())
                MDC.put("htmlContentLength", pageInfo.content.length.toString())
                log.info("HTML parsing completed: feedId=${message.feedId}, duration=${parseDuration}ms")
                
                var thumbnailImage = pageInfo.thumbnailImage
                
                // 4. Gemini 요약
                MDC.put("stage", "gemini_summarization")
                log.info("Starting Gemini summarization: feedId=${message.feedId}")
                val summarizeStart = System.currentTimeMillis()
                
                val folderNames = withContext(Dispatchers.IO + withMDCContext()) {
                    folderService.getFolders(userId = message.userId).map { it.name }
                }
                
                val content = withContext(Dispatchers.IO + withMDCContext()) {
                    geminiService.summarize(
                        content = pageInfo.content,
                        link = message.link,
                        folders = folderNames.joinToString(" "),
                        userId = message.userId,
                        feedId = message.feedId,
                        language = null 
                    )
                }
                
                val summarizeDuration = System.currentTimeMillis() - summarizeStart
                MDC.put("geminiDuration", summarizeDuration.toString())
                log.info("Gemini summarization completed: feedId=${message.feedId}, duration=${summarizeDuration}ms")
                
                // 5. 플랫폼별 이미지 처리
                val brunch = feedService.findBrunch(message.link)
                if (brunch == com.jordyma.blink.feed.domain.Source.BRUNCH) {
                    thumbnailImage = thumbnailImage.removePrefix("//")
                    thumbnailImage = "https://$thumbnailImage"
                }
                
                // 6. DB 저장
                MDC.put("stage", "db_save")
                log.info("Saving summarized feed: feedId=${message.feedId}")
                val saveStart = System.currentTimeMillis()
                
                val feed = withContext(Dispatchers.IO + withMDCContext()) {
                    feedService.updateSummarizedFeed(
                        content = content,
                        brunch = brunch,
                        feedId = message.feedId,
                        userId = message.userId,
                        thumbnailImage = thumbnailImage
                    )
                }
                
                val saveDuration = System.currentTimeMillis() - saveStart
                MDC.put("dbSaveDuration", saveDuration.toString())
                
                // 7. FCM 푸시 알림 
                launch(Dispatchers.IO + withMDCContext()) {
                    MDC.put("stage", "fcm_push")
                    try {
                        val user = userRepository.findById(message.userId).orElse(null)
                        if (user?.iosPushToken != null || user?.aosPushToken != null) {
                            fcmService.sendSummarizedAlert(message.userId, feed)
                            log.info("Push notification sent: feedId=${message.feedId}")
                        }
                    } catch (e: Exception) {
                        log.error("Failed to send push notification: feedId=${message.feedId}", e)
                    }
                }
                
                val totalDuration = System.currentTimeMillis() - startTime
                MDC.put("totalDuration", totalDuration.toString())
                MDC.put("result", "success")
                log.info("V2 Strategy completed: feedId=${message.feedId}, total=${totalDuration}ms, " +
                        "parse=${parseDuration}ms, gemini=${summarizeDuration}ms, save=${saveDuration}ms")
            }
            
        } catch (e: TimeoutCancellationException) {
            val duration = System.currentTimeMillis() - startTime
            MDC.put("totalDuration", duration.toString())
            MDC.put("result", "timeout")
            log.error("Summarization timeout (>2min): feedId=${message.feedId}, duration=${duration}ms")
            
            withContext(Dispatchers.IO + withMDCContext()) {
                val feed = feedRepository.findById(message.feedId).orElse(null)
                feed?.updateStatus(Status.FAILED)
                feed?.let { feedRepository.save(it) }
            }
            
        } catch (e: CancellationException) {
            val duration = System.currentTimeMillis() - startTime
            MDC.put("totalDuration", duration.toString())
            MDC.put("result", "cancelled")
            log.warn("Summarization cancelled: feedId=${message.feedId}, duration=${duration}ms")
            throw e
            
        } catch (e: Exception) {
            val duration = System.currentTimeMillis() - startTime
            MDC.put("totalDuration", duration.toString())
            MDC.put("result", "failed")
            MDC.put("errorType", e.javaClass.simpleName)
            log.error("V2 Strategy failed: feedId=${message.feedId}, duration=${duration}ms, error=${e.message}", e)
            
            // TODO: 메트릭 기록
            // metrics.recordFailure("V2_API_DIRECT", e.javaClass.simpleName)
            
            withContext(Dispatchers.IO + withMDCContext()) {
                val feed = feedRepository.findById(message.feedId).orElse(null)
                feed?.updateStatus(Status.FAILED)
                feed?.let { feedRepository.save(it) }
            }
            
        } finally {
            // 8. 락 해제
            withContext(Dispatchers.IO + withMDCContext()) {
                try {
                    if (lock.isHeldByCurrentThread) {
                        lock.unlock()
                        log.debug("Lock released: feedId=${message.feedId}")
                    }
                } catch (e: Exception) {
                    log.error("Failed to release lock: feedId=${message.feedId}", e)
                }
            }
            
            MDC.clear()
        }
    }
    
    override fun getStrategyName() = "V2_API_DIRECT"
}
