package com.jordyma.blink.feed_summarizer.listener

import com.jordyma.blink.feed_summarizer.listener.dto.FeedSummarizeMessage
import com.jordyma.blink.feed_summarizer.request_limiter.SummarizeRequestLimiter
import com.jordyma.blink.feed_summarizer.service.FeedSummarizerService
import com.jordyma.blink.global.gemini.response.PromptResponse
import io.awspring.cloud.sqs.annotation.SqsListener
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement
import org.springframework.messaging.Message
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.stereotype.Component

@Component
class SummaryRequestListenerImpl(
    private val feedSummarizerService: FeedSummarizerService,
    private val summarizeRequestLimiter: SummarizeRequestLimiter
) : SummaryRequestListener {

    @SqsListener("\${spring.cloud.aws.sqs.summary-request-queue.name}")
    override fun summarizeFeed(
        message: Message<FeedSummarizeMessage>,
        @Headers headers: MessageHeaders,
        acknowledgement: Acknowledgement
    ): PromptResponse? {
        // TOKEN이 남아있을 때에만 요청을 처리한다.
        if (summarizeRequestLimiter.decreaseToken() > 0) {
            val payload = message.payload
            feedSummarizerService.summarizeFeed(payload)

            // 정상적으로 요청을 처리한 경우에만 메세지큐에서 요청을 삭제한다.
            acknowledgement.acknowledge()
        }
        return null
    }
}