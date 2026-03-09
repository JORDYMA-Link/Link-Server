package com.jordyma.blink.folder.domain.model

import com.jordyma.blink.feed.domain.model.FeedDto

data class GetFeedsByFolderRequestDto(
    val cursor: Int?,
    val pageSize: Long = 10,
)

data class GetFeedsByFolderResponseDto(
    val folderId: Long,
    val folderName: String,
    val feedList: List<Any> = mutableListOf<FeedDto>()
)