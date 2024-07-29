package com.jordyma.blink.feed.repository

import com.jordyma.blink.feed.entity.Feed
import com.jordyma.blink.feed.vo.FeedFolderVo
import com.jordyma.blink.folder.entity.Folder
import java.time.LocalDate
import java.time.LocalDateTime

interface CustomFeedRepository {
    fun findFeedFolderDtoByUserIdAndBetweenDate(userId: Long, startOfMonth: LocalDateTime, endOfMonth: LocalDateTime): List<FeedFolderVo>

    fun deleteAllByFolder(folder: Folder): Unit

    fun findAllByFolder(folder: Folder): List<Feed>
}