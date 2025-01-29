package com.jordyma.blink.feed_summarize_requester.sender

import com.jordyma.blink.feed_summarize_requester.sender.dto.FeedSummarizeMessage
import com.jordyma.blink.logger
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
        logger().info("Attempting to send message to queue: $queueName, message: $message")

        return try {
            sqsTemplate.send { to ->
                to.queue(queueName)
                    .payload(message)
            }.also { result ->
                logger().info("Successfully sent message to queue: $queueName, messageId: ${result.messageId}")
            }
        } catch (e: Exception) {
            logger().error("Failed to send message to queue: $queueName, error: ${e.message}", e)
            throw e
        }
    }
}