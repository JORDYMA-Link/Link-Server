package com.jordyma.blink.fcm.service

import com.jordyma.blink.fcm.client.FcmClient
import com.jordyma.blink.logger
import com.jordyma.blink.message.MessageRepository
import com.jordyma.blink.message.PushMessage
import com.jordyma.blink.user.User
import com.jordyma.blink.user.UserRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlinx.coroutines.*

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
            val users: List<User> = userRepository.findActiveMobileUser()

            runBlocking {
                // 50명씩 병렬 처리
                val results = users.chunked(50).flatMap { batch ->
                    batch.map { user ->
                        async(Dispatchers.IO) {
                            sendMessageToUser(user, pendingMessage)
                        }
                    }.awaitAll()
                }

                val successCount = results.count { it == true }
                val failCount = results.count { it == false }
                val noTokenCount = results.count { it == null }

                logger().info("FCM batch completed - Success: $successCount, Failed: $failCount, No Token: $noTokenCount, Total: ${users.size}")
            }
        }
    }

    private suspend fun sendMessageToUser(user: User, pendingMessage: PushMessage): Boolean? {
        return try {
            val token = user.iosPushToken ?: user.aosPushToken
            if (token != null) {
                withTimeout(5000) {
                    val message = fcmClient.createMessage(token, pendingMessage.title, pendingMessage.body, emptyMap())
                    fcmClient.send(message)
                }
                true
            } else {
                logger().warn("User ${user.id} has no push token")
                null // 토큰이 없는 경우
            }
        } catch (e: TimeoutCancellationException) {
            logger().error("FCM send timeout for user ${user.id}")
            false
        } catch (e: Exception) {
            logger().error("Failed to send FCM message to user ${user.id}: ${e.message}", e)
            false
        }
    }

}