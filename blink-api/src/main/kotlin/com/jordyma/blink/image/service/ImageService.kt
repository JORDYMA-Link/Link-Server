package com.jordyma.blink.image.service

import com.jordyma.blink.feed.domain.Feed
import com.jordyma.blink.feed.domain.FeedRepository
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.global.s3.S3Uploader
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

@Service
class ImageService (
    private val s3Uploader: S3Uploader,
    private val feedRepository: FeedRepository,
){
    // 썸네일 이미지 업로드
    @Throws(IOException::class)
    fun uploadThumbnailImage(image: MultipartFile, feedId: Long): String {
        val feed = findFeedOrElseThrow(feedId)
        val imageUrl = s3Uploader.s3UploadOfThumbnailImage(feed, image)
        feed.updateThumbnailImageUrl(imageUrl)
        feedRepository.save(feed)
        return imageUrl
    }

    fun findFeedOrElseThrow(feedId: Long): Feed {
        return feedRepository.findById(feedId).orElseThrow {
            ApplicationException(ErrorCode.FEED_NOT_FOUND, "피드를 찾을 수 없습니다.")
        }
    }
}