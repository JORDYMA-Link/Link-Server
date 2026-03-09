package com.jordyma.blink.feed.usecase

import com.jordyma.blink.common.exception.BlinkException
import com.jordyma.blink.common.exception.ErrorCode
import com.jordyma.blink.common.logger.BlinkLogger
import com.jordyma.blink.feed.domain.Feed
import com.jordyma.blink.feed.domain.FeedRepository
import com.jordyma.blink.feed.domain.Source
import com.jordyma.blink.feed.domain.Status
import com.jordyma.blink.feed.domain.model.FeedSummarizeMessage
import com.jordyma.blink.feed.domain.service.ContentSummarizer
import com.jordyma.blink.feed.domain.service.FeedSummarizeService
import com.jordyma.blink.feed.domain.service.PageParser
import com.jordyma.blink.folder.domain.service.FolderService
import com.jordyma.blink.notification.service.SendNotificationService
import com.jordyma.blink.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class SummarizeFeedUsecase (
    private val feedRepository: FeedRepository,
    private val userRepository: UserRepository,
    private val htmlParser: PageParser,
    private val contentSummarizer: ContentSummarizer,
    private val folderService: FolderService,
    private val feedSummarizeService: FeedSummarizeService,
    private val sendNotificationService: SendNotificationService,
    private val logger: BlinkLogger,
){
    @Transactional
    fun summarizeFeed(input: FeedSummarizeMessage) {
        val userId = input.userId
        val link = input.link
        val feedId = input.feedId

        try {
            val parseContent = htmlParser.parseUrl(link)
            var thumbnailImage = parseContent.thumbnailImage
            val folderNames: List<String> = folderService.getFolders(userId).map { it.name }
            val content = contentSummarizer.summarize(
                parseContent.content,
                link = link,
                folders = folderNames.joinToString(separator = " "),
                userId = userId,
                feedId
            )

            // 플랫폼별 이미지 추출
            val brunch = findBrunch(link)
            if (brunch == Source.BRUNCH){
                thumbnailImage = thumbnailImage.removePrefix("//")
            }

            // 요약 결과 업데이트
            val feed = feedSummarizeService.updateSummarizedFeed(
                content,
                brunch,
                feedId,
                userId,
                thumbnailImage,
            )

            // 요약 완료 푸시알림 전송
            val user = userRepository.findById(userId).orElseThrow { BlinkException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다") }
            if (user.iosPushToken != null || user.aosPushToken != null) {
                sendNotificationService.sendSummarizedAlert(userId, feed)
            }
        } catch (e: Exception) {
            val feed = findFeedOrElseThrow(feedId)
            feed.updateStatus(Status.FAILED)
            feedRepository.save(feed)
            e.message?.let { logger.error(it) }
            logger.info("gemini exception: failed to summarize ${input.originUrl} by userName ${input.userName}")
        }
    }

    private fun findBrunch(link: String = ""): Source {
        return if(link.contains("blog.naver.com")){
            Source.NAVER_BLOG
        } else if (link.contains("velog.io")){
            Source.VELOG
        } else if (link.contains("brunch.co.kr")){
            Source.BRUNCH
        } else if (link.contains("yozm.wishket")){
            Source.YOZM_IT
        } else if (link.contains("tistory.com")){
            Source.TISTORY
        } else if (link.contains("eopla.net")){
            Source.EO
        } else if (link.contains("youtube.com")) {
            Source.YOUTUBE
        } else if (link.contains("naver.com")) {
            Source.NAVER
        } else if (link.contains("google.com")) {
            Source.GOOGLE
        } else {
            Source.DEFAULT
        }
    }

    private fun findFeedOrElseThrow(feedId: Long): Feed {
        return feedRepository.findById(feedId).orElseThrow {
            BlinkException(ErrorCode.FEED_NOT_FOUND, "피드를 찾을 수 없습니다.")
        }
    }
}