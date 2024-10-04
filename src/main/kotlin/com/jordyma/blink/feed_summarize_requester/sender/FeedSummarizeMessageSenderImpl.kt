package com.jordyma.blink.feed_summarize_requester.sender

import com.jordyma.blink.feed_summarize_requester.sender.dto.FeedSummarizeMessage
import io.awspring.cloud.sqs.operations.SendResult
import io.awspring.cloud.sqs.operations.SqsTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class FeedSummarizeMessageSenderImpl(
    @Value("\${spring.cloud.aws.sqs.summary-request-queue.name}") private val queueName: String,
    private val sqsTemplate: SqsTemplate,
): FeedSummarizeMessageSender {

    override fun send(message: FeedSummarizeMessage): SendResult<FeedSummarizeMessage> {
        return sqsTemplate.send { to ->
            to
                .queue(queueName)
                .payload(message)
        }
    }
}