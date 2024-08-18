package com.jordyma.blink.image.service

import com.jordyma.blink.feed.entity.Feed
import com.jordyma.blink.feed.repository.FeedRepository
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.global.s3.S3Uploader
import com.jordyma.blink.image.dto.response.ImageCreateResDto
import com.jordyma.blink.image.repository.ThumbnailImageRepository
import com.jordyma.blink.user.entity.User
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

@Service
class ImageService (
    private val s3Uploader: S3Uploader,
    private val feedRepository: FeedRepository,
    private val thumbnailImageRepository: ThumbnailImageRepository
    ){

    // 썸네일 이미지 업로드
    @Throws(IOException::class)
    public fun uploadThumbnailImage(image: MultipartFile, feedId: Long): ImageCreateResDto{
        val feed = findFeedOrElseThrow(feedId)
        val imageUrl = s3Uploader.s3UploadOfThumbnailImage(feed, image)
        val thumbnailImage = ThumbnailImage(
            feed = feed,
            url = imageUrl
        )
        thumbnailImageRepository.save(thumbnailImage)
        return ImageCreateResDto(imageUrl = thumbnailImage.url)
    }

    fun findFeedOrElseThrow(feedId: Long): Feed {
        return feedRepository.findById(feedId).orElseThrow {
            ApplicationException(ErrorCode.FEED_NOT_FOUND, "피드를 찾을 수 없습니다.")
        }
    }
}