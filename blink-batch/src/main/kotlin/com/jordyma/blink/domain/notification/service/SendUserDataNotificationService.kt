package com.jordyma.blink.domain.notification.service

import com.jordyma.blink.domain.notification.dto.UserDataNotificationDto
import com.jordyma.blink.logger
import com.jordyma.blink.stats.service.LinkStatsProcessService
import org.springframework.beans.factory.annotation.Qualifier
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
class SendUserDataNotificationService(
    @Value("\${slack.channel-id}")
    private val channelId: String,
    @Value("\${slack.token}")
    private val botToken: String,
    @Qualifier("slackRestTemplate")
    private val restTemplate: RestTemplate,
    @Qualifier("linkStatsProcessServiceImpl")
    private val linkStatsProcessService : LinkStatsProcessService,
) {
    fun sendUserDataNotification(data: UserDataNotificationDto) {

        val yesterday = LocalDate.now().minusDays(1)
        val formattedDate = yesterday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        val linkViewCount = linkStatsProcessService.getYesterdayLinkViewCount()
        val activeUsers = linkStatsProcessService.getYesterdayDailyActiveUsers()

        val message = """
             *${formattedDate} 사용자 보고서*
            - 신규 사용자 +${data.newUserCount} (누적 ${data.totalUserCount})
            - 신규 사용자 링크 저장 +${data.newUserFeed}
            - 기존 사용자 링크 저장 +${data.existingUserFeed}
            
            - 링크 저장 클릭 횟수 : $linkViewCount
            - 활성 사용자수 : $activeUsers
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