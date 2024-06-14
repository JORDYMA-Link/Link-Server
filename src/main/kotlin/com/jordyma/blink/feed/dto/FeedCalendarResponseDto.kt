package com.jordyma.blink.feed.dto


data class FeedCalendarResponseDto(
    val isArchived: Boolean,
    val list: List<FeedItem>
)

data class FeedItem(
    val folderId: Long,
    val folderName: String,
    val feedId: Long,
    val title: String,
    val summary: String,
    val source: String,
    val sourceUrl: String,
    val isMarked: Boolean,
    val keywords: List<String>
)