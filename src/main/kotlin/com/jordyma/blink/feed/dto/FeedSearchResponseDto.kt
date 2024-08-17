package com.jordyma.blink.feed.dto

data class FeedSearchResponseDto(
    val query: String,
    val result: List<FeedResultDto>
)

data class FeedResultDto(
    val feedId: Long,
    val title: String,
    val summary: String,
    val platform: String,
    val platformImage: String,
    val isMarked: Boolean,
    val keywords: List<String>
)
