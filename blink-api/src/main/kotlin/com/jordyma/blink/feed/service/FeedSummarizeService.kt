package com.jordyma.blink.feed.service

import com.jordyma.blink.fcm.service.FcmService
import com.jordyma.blink.feed.domain.FeedRepository
import com.jordyma.blink.feed.domain.Source
import com.jordyma.blink.feed.domain.Status
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
import org.springframework.stereotype.Service

@Service
class FeedSummarizeService(
    private val feedRepository: FeedRepository,
    private val folderRepository: FolderRepository,
    private val userRepository: UserRepository,
    private val htmlParser: HtmlParserByJsoup,
    private val geminiService: GeminiService,
    private val folderService: FolderService,
    private val feedService: FeedService,
    private val fcmService: FcmService,
){

    fun summarizeFeed(payload: FeedSummarizeMessage): PromptResponse? {
        val userId = payload.userId
        val link = payload.link
        val feedId = payload.feedId.toLong()

        try{
            val parseContent = htmlParser.parseUrl(link)
            var thumbnailImage = parseContent.thumbnailImage
            val folderNames: List<String> = folderService.getFolders(userId=userId).map { it.name }
            val content = geminiService.summarize(
                link = link,
                folders = folderNames.joinToString(separator = " "),
                userId = userId,
                content = parseContent.content,
                feedId = feedId,
            )

            // 플랫폼별 이미지 추출
            val brunch = feedService.findBrunch(link)
            if (brunch == Source.BRUNCH){
                thumbnailImage = thumbnailImage.removePrefix("//")
            }

            // 요약 결과 업데이트
            val feed = feedService.updateSummarizedFeed(
                content,
                brunch,
                feedId,
                userId,
                thumbnailImage,
            )

            // 요약 완료 푸시알림 전송
            val user = userRepository.findById(userId).orElseThrow { BadRequestException(USER_NOT_FOUND) }
            if (user.iosPushToken != null || user.aosPushToken != null) {
                fcmService.sendSummarizedAlert(userId, feed)
            }
        } catch (e: Exception){
            val feed = feedService.findFeedOrElseThrow(feedId)
            feed.updateStatus(Status.FAILED)
            feedRepository.save(feed)
            logger().error(e.message)
            logger().info("gemini exception: failed to summarize ${payload.originUrl} by userName ${payload.userName}")
        }
        // TODO: exception 되돌리기
        return null
    }

}