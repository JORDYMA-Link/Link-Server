package com.jordyma.blink.feed.domain.model

data class FeedSummarizeMessage (
    val link: String,
    val feedId: Long,
    val userId: Long,
    val userName: String,
    val originUrl: String,
)