package com.jordyma.blink.feed_summarizer.service

import com.jordyma.blink.fcm.service.FcmService
import com.jordyma.blink.feed.domain.Source
import com.jordyma.blink.feed.domain.Status
import com.jordyma.blink.feed.domain.FeedRepository
import com.jordyma.blink.feed.service.FeedService
import com.jordyma.blink.feed_summarizer.html_parser.HtmlParser
import com.jordyma.blink.feed_summarizer.html_parser.HtmlParserV2
import com.jordyma.blink.feed_summarizer.idempotency.IdempotencyService
import com.jordyma.blink.feed_summarizer.listener.dto.FeedSummarizeMessage
import com.jordyma.blink.feed_summarizer.request_limiter.SummarizeRequestLimiter
import com.jordyma.blink.folder.service.FolderService
import com.jordyma.blink.gemini.GeminiService
import com.jordyma.blink.global.error.USER_NOT_FOUND
import com.jordyma.blink.global.error.exception.BadRequestException
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.gemini.response.PromptResponse
import com.jordyma.blink.logger
import com.jordyma.blink.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FeedSummarizerServiceImpl(
    private val summarizeRequestLimiter: SummarizeRequestLimiter,
    private val htmlParser: HtmlParser,
    private val htmlParserV2: HtmlParserV2,
    private val folderService: FolderService,
    private val geminiService: GeminiService,
    private val feedService: FeedService,
    private val feedRepository: FeedRepository,
    private val userRepository: UserRepository,
    private val fcmService: FcmService,
    private val idempotencyService: IdempotencyService,
): FeedSummarizerService {

    override fun summarizeFeed(payload: FeedSummarizeMessage): PromptResponse? {
        val userId = payload.userId
        val link = payload.link
        val feedId = payload.feedId.toLong()

        // 멱등성 체크: 10분 이내 동일 요청이면 중복 처리 방지
        if (idempotencyService.isDuplicate(userId, link)) {
            logger().info("Skipping duplicate request: userId=$userId, link=$link, feedId=$feedId")
            return null
        }

        // 멱등키 마킹: 처리 시작
        idempotencyService.markAsProcessing(userId, link)

        try {
            val parseContent = htmlParserV2.parseUrl(link)
            var thumbnailImage = parseContent.thumbnailImage
            val folderNames: List<String> = folderService.getFolders(userId = userId).map { it.name }

            val content = geminiService.getContents(
                link = link,
                folders = folderNames.joinToString(separator = " "),
                userId = userId,
                parseContent.content,
                feedId
            )
            if (content == null) {
                throw ApplicationException(ErrorCode.JSON_PARSING_FAILED, "gemini exception: no content")
            }

            val brunch = feedService.findBrunch(link)
            if (brunch == Source.BRUNCH) {
                thumbnailImage = thumbnailImage.removePrefix("//")
            }

            val feed = saveFeedResultInTransaction(
                content = content,
                brunch = brunch,
                feedId = feedId,
                userId = userId,
                thumbnailImage = thumbnailImage
            )

            idempotencyService.markAsCompleted(userId, link)

            sendNotificationIfNeeded(userId, feed)

        } catch (e: Exception) {
            idempotencyService.clearIdempotencyKey(userId, link)

            markFeedAsFailedInTransaction(feedId)

            logger().error(e.message)
            logger().info("gemini exception: failed to summarize ${payload.originUrl} by userName ${payload.userName}")
        }
        return null
    }


    @Transactional
    private fun saveFeedResultInTransaction(
        content: PromptResponse,
        brunch: Source,
        feedId: Long,
        userId: Long,
        thumbnailImage: String
    ) = feedService.updateSummarizedFeed(
        content.subject,
        content.summary,
        content.category,
        content.keyword,
        brunch,
        feedId,
        userId,
        thumbnailImage
    )


    @Transactional
    private fun markFeedAsFailedInTransaction(feedId: Long) {
        val feed = feedService.findFeedOrElseThrow(feedId)
        feed.updateStatus(Status.FAILED)
        feedRepository.save(feed)
    }


    private fun sendNotificationIfNeeded(userId: Long, feed: com.jordyma.blink.feed.domain.Feed) {
        val user = userRepository.findById(userId).orElseThrow { BadRequestException(USER_NOT_FOUND) }
        if (user.iosPushToken != null || user.aosPushToken != null) {
            fcmService.sendSummarizedAlert(userId, feed)
        }
    }

    override fun refillToken(): Unit {
        this.summarizeRequestLimiter.refillToken()
        logger().info("refill token")
    }
}