package com.jordyma.blink.feed.dto

data class FeedTypeResponseDto(
    val feedId: Long,
    val title: String,
    val summary: String,
    val platform: String,
    val platformImage: String,
    val isMarked: Boolean,
    val keywords: List<String>,
    val recommendedFolder: List<String>? = null
)

