package com.jordyma.blink.feed.repository

import com.jordyma.blink.feed.entity.Feed
import com.jordyma.blink.feed.vo.FeedDetailVo
import com.jordyma.blink.feed.vo.FeedFolderVo
import com.jordyma.blink.folder.entity.Folder
import com.jordyma.blink.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

interface FeedRepositoryCustom {
    fun findFeedFolderDtoByUserIdAndBetweenDate(user: User, startOfMonth: LocalDateTime, endOfMonth: LocalDateTime): List<FeedFolderVo>

    fun findFeedDetail(user: User, feedId: Long): FeedDetailVo?

    fun deleteAllByFolder(folder: Folder): Long

    fun getProcessing(user: User): List<Feed>

    fun findAllByFolder(folder: Folder, cursor: Int?, pageSize: Long): List<Feed>

    fun findAllByUser(user: User, pageable: Pageable): Page<Feed>

    fun findUnclassifiedFeeds(userId: Long, pageable: Pageable): Page<Feed>

    fun findBookmarkedFeeds(userId: Long, pageable: Pageable): Page<Feed>

    fun findFeedByQuery(userId: Long, query: String, pageable: Pageable): Page<Feed>

    fun deleteKeywords(folder: Folder): Long

    fun deleteRecommend(folder: Folder): Long
}