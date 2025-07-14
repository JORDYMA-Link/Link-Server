package com.jordyma.blink.fcm.service

import com.jordyma.blink.fcm.client.FcmClient
import com.jordyma.blink.message.MessageRepository
import com.jordyma.blink.user.User
import com.jordyma.blink.user.UserRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.log

@Component
class FcmScheduler(
    private val fcmClient: FcmClient,
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
) {

    @Scheduled(cron = "0 0 * * * *")
    fun sendScheduledMessages() {
        val now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).toString()
        val pendingMessage = messageRepository.findPendingMessages(now)

        if (pendingMessage != null) {
            // 푸시알림 메시지 생성
            val users: List<User> = userRepository.findActiveUser()
            for (user in users) {
                val token = user.iosPushToken ?: user.aosPushToken
                if (token != null) {
                    val message = fcmClient.createMessage(token, pendingMessage.title, pendingMessage.body, emptyMap())
                    fcmClient.send(message)
                }
            }
        }

    }
}