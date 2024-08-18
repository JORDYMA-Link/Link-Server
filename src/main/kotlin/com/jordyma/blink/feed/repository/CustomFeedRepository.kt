package com.jordyma.blink.feed.repository

import com.jordyma.blink.feed.entity.Feed
import com.jordyma.blink.feed.vo.FeedDetailVo
import com.jordyma.blink.feed.vo.FeedFolderVo
import com.jordyma.blink.folder.entity.Folder
import com.jordyma.blink.user.entity.User
import java.time.LocalDateTime

interface CustomFeedRepository {
    fun findFeedFolderDtoByUserIdAndBetweenDate(userId: Long, startOfMonth: LocalDateTime, endOfMonth: LocalDateTime): List<FeedFolderVo>

    fun findFeedDetail(memberId: Long, feedId: Long): FeedDetailVo?

    fun deleteAllByFolder(folder: Folder): Long

    fun findAllByFolder(folder: Folder): List<Feed>

    fun getProcessing(user: User): List<Feed>
}