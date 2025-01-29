package com.jordyma.blink.feed_summarizer.controller

import org.springframework.http.ResponseEntity

interface FeedSummarizerController {

    fun refillToken(): ResponseEntity<Unit>
}