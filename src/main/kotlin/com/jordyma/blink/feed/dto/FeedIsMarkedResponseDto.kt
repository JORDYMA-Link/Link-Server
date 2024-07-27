package com.jordyma.blink.feed.dto

data class FeedIsMarkedResponseDto(
    val id: Long,
    val isMarked: Boolean,
    val modifiedDate: String
)
