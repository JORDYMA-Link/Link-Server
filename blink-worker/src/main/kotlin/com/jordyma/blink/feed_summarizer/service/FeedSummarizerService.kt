package com.jordyma.blink.feed_summarizer.service

import com.jordyma.blink.feed_summarizer.listener.dto.FeedSummarizeMessage
import com.jordyma.blink.gemini.response.PromptResponse

interface FeedSummarizerService {

        fun summarizeFeed(payload: FeedSummarizeMessage): PromptResponse?

        fun refillToken(): Unit
}