package com.jordyma.blink.feed_summarizer.controller

import com.jordyma.blink.feed_summarizer.service.FeedSummarizerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/feed-summarizer")
class FeedSummarizerControllerImpl(
    private val feedSummarizerService: FeedSummarizerService
): FeedSummarizerController {

    @PostMapping("/refill-token")
    override fun refillToken(): ResponseEntity<Unit> {
        this.feedSummarizerService.refillToken()

        return ResponseEntity.ok().build()
    }
}