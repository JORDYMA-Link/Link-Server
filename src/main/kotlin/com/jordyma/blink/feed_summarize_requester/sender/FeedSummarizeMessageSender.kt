package com.jordyma.blink.feed_summarize_requester.sender

import com.jordyma.blink.feed_summarize_requester.sender.dto.FeedSummarizeMessage
import io.awspring.cloud.sqs.operations.SendResult

interface FeedSummarizeMessageSender {

    fun send(message: FeedSummarizeMessage): SendResult<FeedSummarizeMessage>
}