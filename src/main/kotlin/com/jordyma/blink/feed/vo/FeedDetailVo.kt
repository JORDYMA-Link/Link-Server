package com.jordyma.blink.feed.vo

import java.time.LocalDateTime

data class FeedDetailVo(
    val feedId: Long,
    val thumbnailImageUrl: String?,
    val title: String,
    val date: LocalDateTime,
    val summary: String,
    val platform: String,
    val folderName: String,
    val memo: String? = "",
    val isMarked: Boolean,
    val originUrl: String,
)
