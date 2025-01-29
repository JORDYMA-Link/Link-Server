package com.jordyma.blink.feed_summarizer.request_limiter

interface SummarizeRequestLimiter {

    fun decreaseToken(): Long

    fun refillToken(): Unit
}