package com.jordyma.blink.feed.dto


data class FeedDateResponseDto(
    val date: String,
    val count: Int,
    val feedsInFolderList: List<FeedsInFolder>,
)

data class FeedsInFolder(
    val folderId: Long,
    val folderName: String,
    val folderCount: Int,
    val feedList: List<FeedItem>
)

data class FeedItem(
    val feedId: Long,
    val title: String,
    val summary: String,
    val source: String,
    val sourceUrl: String,
    val isMarked: Boolean,
    val keywords: List<String>
)