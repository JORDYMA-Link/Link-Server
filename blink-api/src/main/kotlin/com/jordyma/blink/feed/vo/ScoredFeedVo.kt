package com.jordyma.blink.feed.vo

import com.jordyma.blink.feed.entity.Feed

data class ScoredFeedVo(
    val feed: Feed,
    val score: Double
)
