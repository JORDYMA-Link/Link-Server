package com.jordyma.blink.feed_summarizer.listener

import com.jordyma.blink.feed_summarizer.listener.dto.FeedSummarizeMessage
import com.jordyma.blink.gemini.response.PromptResponse
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement
import org.springframework.messaging.Message
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.handler.annotation.Headers

interface SummaryRequestListener {

    fun summarizeFeed(message: Message<FeedSummarizeMessage>, @Headers headers: MessageHeaders, acknowledgement: Acknowledgement): PromptResponse?
}