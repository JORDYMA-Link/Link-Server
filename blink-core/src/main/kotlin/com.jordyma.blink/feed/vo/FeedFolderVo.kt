package com.jordyma.blink.feed.vo

import com.jordyma.blink.feed.domain.Feed

data class FeedFolderVo(
    val folderId: Long,
    val folderName: String,
    val feed: Feed,
)