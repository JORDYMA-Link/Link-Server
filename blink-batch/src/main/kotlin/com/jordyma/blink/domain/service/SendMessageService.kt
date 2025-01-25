package com.jordyma.blink.domain.service

import com.jordyma.blink.domain.dto.UserDataNotificationDto
import com.jordyma.blink.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class SendMessageService(
    @Value("\${slack.channel-id}")
    private val channelId: String,
    @Value("\${slack.token}")
    private val botToken: String,
    private val restTemplate: RestTemplate
) {
    fun sendUserDataNotification(data: UserDataNotificationDto) {

        val yesterday = LocalDate.now().minusDays(1)
        val formattedDate = yesterday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val message = """
             *${formattedDate} 사용자 보고서*
            ㅤ- 신규 사용자 +${data.newUserCount} (누적 ${data.totalUserCount})
            ㅤ- 신규 사용자 링크 저장 +${data.newUserFeed}
            ㅤ- 기존 사용자 링크 저장 +${data.existingUserFeed}
        """.trimIndent()

        val url = UriComponentsBuilder
            .fromHttpUrl("https://slack.com/api/chat.postMessage")
            .build()
            .toUri()

        val headers = HttpHeaders().apply {
            setBearerAuth(botToken)
            contentType = MediaType.APPLICATION_JSON
        }

        val body = mapOf(
            "channel" to channelId,
            "text" to message
        )

        val request = HttpEntity(body, headers)

        try {
            val response = restTemplate.postForEntity(url, request, String::class.java)
            logger().info("Slack API Response: ${response.body}")

            if (response.statusCode != HttpStatus.OK) {
                logger().error("Failed to send message to Slack. Status: ${response.statusCode}, Body: ${response.body}")
                throw RuntimeException("Failed to send message to Slack")
            }
        } catch (e: Exception) {
            logger().error("Failed to send message to Slack: ${e.message}", e)
            throw e
        }
    }
}