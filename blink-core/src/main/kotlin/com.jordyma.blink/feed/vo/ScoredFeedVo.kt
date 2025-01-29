package com.jordyma.blink.feed.vo
import com.jordyma.blink.feed.Feed

data class ScoredFeedVo(
    val feed: Feed,
    val score: Double
)
