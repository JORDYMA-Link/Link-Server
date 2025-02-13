package com.jordyma.blink.feed.domain.model

data class FeedDto(
    val folderId: Long?,
    val folderName: String,
    val feedId: Long,
    val title: String,
    val summary: String,
    val platform: String,
    val platformImage: String,
    val sourceUrl: String,
    val isMarked: Boolean,
    val keywords: List<String>
)