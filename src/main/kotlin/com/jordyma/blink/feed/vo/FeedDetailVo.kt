package com.jordyma.blink.feed.vo

import java.time.LocalDateTime

data class FeedDetailVo(
    val feedId: Long,
    val thumnailImage: String,
    val title: String,
    val date: LocalDateTime,
    val summary: String,
    val folderName: String,
    val memo: String
)
