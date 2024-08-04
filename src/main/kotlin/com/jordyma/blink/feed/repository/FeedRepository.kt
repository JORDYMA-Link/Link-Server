package com.jordyma.blink.feed.repository

import com.jordyma.blink.feed.entity.Feed
import com.jordyma.blink.folder.entity.Folder
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface FeedRepository : JpaRepository<Feed, Long>, CustomFeedRepository {

    override fun deleteAllByFolder(folder: Folder): Unit

    override fun findAllByFolder(folder: Folder): List<Feed>

    @Query("SELECT fd FROM Feed fd JOIN Folder fdr ON fd.folder.id = fdr.id " +
            "WHERE fdr.user.id = :userId AND fd.isMarked = true")
    fun findBookmarkedFeeds(userId: Long, pageable: Pageable): Page<Feed>

    @Query("SELECT fd FROM Feed fd JOIN Folder fdr ON fd.folder.id = fdr.id " +
            "WHERE fdr.user.id = :userId AND fdr.isUnclassified = true")
    fun findUnclassifiedFeeds(userId: Long, pageable: Pageable): Page<Feed>
}

