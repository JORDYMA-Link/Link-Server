package com.jordyma.blink.feed_summarizer.listener

import com.jordyma.blink.feed_summarizer.listener.dto.FeedSummarizeMessage
import com.jordyma.blink.feed_summarizer.request_limiter.SummarizeRequestLimiter
import com.jordyma.blink.feed_summarizer.service.FeedSummarizerService
import com.jordyma.blink.gemini.response.PromptResponse
import com.jordyma.blink.logger
import io.awspring.cloud.sqs.annotation.SqsListener
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.messaging.Message
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

@Component
class SummaryRequestListenerImpl(
    private val feedSummarizerService: FeedSummarizerService,
    private val summarizeRequestLimiter: SummarizeRequestLimiter
) : SummaryRequestListener {

    private val log = LoggerFactory.getLogger(this::class.java)

    @SqsListener("\${spring.cloud.aws.sqs.summary-request-queue}")
    override fun summarizeFeed(
        message: Message<FeedSummarizeMessage>,
        @Headers headers: MessageHeaders,
        acknowledgement: Acknowledgement
    ): PromptResponse? {

        val remainingTokens = summarizeRequestLimiter.getRemainingToken()
        log.info("Processing SQS message: messageId=${headers.id}, remainingTokens=$remainingTokens")

        // 토큰이 있는지 확인
        if (summarizeRequestLimiter.decreaseToken() > 0) {
            // 토큰이 있으면 요약 처리
            log.info("Processing message: messageId=${headers.id}")
            val payload = message.payload
            feedSummarizerService.summarizeFeed(payload)
            log.info("Successfully processed message: messageId=${headers.id}")

            // 정상적으로 요청을 처리한 경우에만 메시지큐에서 요청을 삭제
            acknowledgement.acknowledge()
        } else {
            // 토큰이 없으면 대기
            handleTokenExhaustion(message, acknowledgement)
        }
        
        return null
    }
    
    /**
     * 토큰 소진 시 대기
     * - 다음 리필 시점까지 스레드를 재움
     */
    private fun handleTokenExhaustion(
        message: Message<FeedSummarizeMessage>,
        acknowledgement: Acknowledgement
    ) {
        val sleepDuration = calculateSleepUntilRefill()
        
        log.info("Token exhausted. Waiting for refill: sleepDuration=${sleepDuration}ms, feedId=${message.payload.feedId}")
        
        // 메시지 삭재 -> visibility timeout 이후 복구됨
        acknowledgement.acknowledge() 
        
        runBlocking {
            delay(sleepDuration)
        }
        
        log.info("Woke up after token refill wait: feedId=${message.payload.feedId}")
    }
    
    /**
     * 다음 토큰 리필 시점까지 남은 시간 계산
     */
    private fun calculateSleepUntilRefill(): Long {
        val now = Instant.now()
        val nextRefillTime = now.truncatedTo(ChronoUnit.MINUTES).plusSeconds(60)
        val duration = Duration.between(now, nextRefillTime).toMillis()
        
        return duration.coerceIn(1000L, 60_000L)
    }
}