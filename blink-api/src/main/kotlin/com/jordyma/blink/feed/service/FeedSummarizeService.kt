package com.jordyma.blink.feed.service

import com.jordyma.blink.common.system.CommonParameterCode.EXCEPTION_LINK_PARAM_CODE
import com.jordyma.blink.common.system.CommonParameterRepository
import com.jordyma.blink.fcm.service.FcmService
import com.jordyma.blink.feed.domain.FeedRepository
import com.jordyma.blink.feed.domain.Source
import com.jordyma.blink.feed.domain.Status
import com.jordyma.blink.feed.idempotency.IdempotencyService
import com.jordyma.blink.feed_summarize_requester.sender.dto.FeedSummarizeMessage
import com.jordyma.blink.folder.FolderRepository
import com.jordyma.blink.folder.domain.service.FolderService
import com.jordyma.blink.global.error.USER_NOT_FOUND
import com.jordyma.blink.global.error.exception.BadRequestException
import com.jordyma.blink.infra.gemini.response.PromptResponse
import com.jordyma.blink.global.util.HtmlParserByJsoup
import com.jordyma.blink.infra.gemini.GeminiService
import com.jordyma.blink.logger
import com.jordyma.blink.user.UserRepository
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FeedSummarizeService(
    private val commonParamRepository: CommonParameterRepository,
    private val feedRepository: FeedRepository,
    private val folderRepository: FolderRepository,
    private val userRepository: UserRepository,
    private val htmlParser: HtmlParserByJsoup,
    private val geminiService: GeminiService,
    private val folderService: FolderService,
    private val feedService: FeedService,
    private val fcmService: FcmService,
    private val idempotencyService: IdempotencyService,
){
    private lateinit var cachedInvalidLinks: List<String>

     fun summarizeFeed(payload: FeedSummarizeMessage): PromptResponse? {
        val userId = payload.userId
        val link = payload.link
        val feedId = payload.feedId
        val language = payload.language

        // 멱등성 체크: 10분 이내 동일 요청이면 중복 처리 방지
        if (idempotencyService.isDuplicate(userId, link)) {
            logger().info("Skipping duplicate request: userId=$userId, link=$link, feedId=$feedId")
            return null
        }

        // 멱등키 마킹: 처리 시작
        idempotencyService.markAsProcessing(userId, link)

        try {
            // 1. 외부 API 호출 (트랜잭션 밖) - 5~10초
            val parseContent = htmlParser.parseUrl(link)
            var thumbnailImage = parseContent.thumbnailImage
            val folderNames: List<String> = folderService.getFolders(userId = userId).map { it.name }

            // 2. LLM API 호출 (트랜잭션 밖) - 10~30초
            val content = geminiService.summarize(
                link = link,
                folders = folderNames.joinToString(separator = " "),
                userId = userId,
                content = parseContent.content,
                feedId = feedId,
                language = language,
            )

            // 3. 플랫폼별 이미지 추출
            val brunch = feedService.findBrunch(link)
            if (brunch == Source.BRUNCH) {
                thumbnailImage = thumbnailImage.removePrefix("//")
                thumbnailImage = "https://$thumbnailImage"
            }

            // 4. DB 작업 (트랜잭션으로 묶음) - 0.1초만 커넥션 점유
            val feed = saveFeedResultInTransaction(
                content = content,
                brunch = brunch,
                feedId = feedId,
                userId = userId,
                thumbnailImage = thumbnailImage
            )

            // 5. 멱등키 마킹: 처리 완료
            idempotencyService.markAsCompleted(userId, link)

            // 6. 외부 API 호출 (트랜잭션 밖) - 1~2초
            sendNotificationIfNeeded(userId, feed)

        } catch (e: Exception) {
            // 실패 시 멱등키 삭제 (재시도 가능하도록)
            idempotencyService.clearIdempotencyKey(userId, link)

            // DB 상태 업데이트 (트랜잭션으로 묶음)
            markFeedAsFailedInTransaction(feedId)

            logger().error(e.message)
            logger().info("gemini exception: failed to summarize ${payload.originUrl} by userName ${payload.userName} by userId ${payload.userId}")
        }
        return null
    }

    /**
     * DB 작업만 트랜잭션으로 묶음 (0.1초만 커넥션 점유)
     */
    @Transactional
    private fun saveFeedResultInTransaction(
        content: PromptResponse,
        brunch: Source,
        feedId: Long,
        userId: Long,
        thumbnailImage: String
    ) = feedService.updateSummarizedFeed(
        content,
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

    @PostConstruct
    fun loadInvalidLinks() {
        cachedInvalidLinks = commonParamRepository.findByParamCode(EXCEPTION_LINK_PARAM_CODE).map { it.paramValue }
        logger().info(">>>>> cachedInvalidLinks: $cachedInvalidLinks")
    }

    fun isInvalidLink(link: String): Boolean {
        for (invalidLink in cachedInvalidLinks){
            if (link.contains(invalidLink)){
                return true
            }
        }
        return false
    }

}