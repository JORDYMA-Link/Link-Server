package com.jordyma.blink.folder.dto

data class FolderCountDto(
    val folderId: Long,
    val countByFolder: Int,
    val countByFeed: Int,
)