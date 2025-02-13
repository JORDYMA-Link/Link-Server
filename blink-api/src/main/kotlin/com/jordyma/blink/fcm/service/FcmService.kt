package com.jordyma.blink.fcm.service

import com.jordyma.blink.fcm.client.FcmClient
import com.jordyma.blink.feed.domain.Feed
import com.jordyma.blink.feed.service.FeedService
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.user.User
import com.jordyma.blink.user.UserRepository
import org.springframework.stereotype.Service

@Service
class FcmService (
    private val fcmClient: FcmClient,
    private val userRepository: UserRepository,
){
    fun sendSummarizedAlert(userId: Long, feed: Feed){
        val user = findUserOrElseThrow(userId)
        val fcmToken = user.iosPushToken ?: user.aosPushToken ?: ""
        val message = fcmClient.createMessage(
            fcmToken,
            FeedService.SUMMARY_COMPLETED,
            "# " + feed.title,
            emptyMap()
        )
        fcmClient.send(message)
    }

    fun findUserOrElseThrow(userId: Long): User {
        return userRepository.findById(userId).orElseThrow {
            ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.")
        }
    }
}