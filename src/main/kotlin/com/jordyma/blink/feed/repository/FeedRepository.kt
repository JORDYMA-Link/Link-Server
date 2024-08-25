package com.jordyma.blink.feed.repository

import com.jordyma.blink.feed.entity.Feed
import com.jordyma.blink.folder.entity.Folder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface FeedRepository : JpaRepository<Feed, Long>, FeedRepositoryCustom {

    override fun deleteAllByFolder(folder: Folder): Long

    override fun findAllByFolder(folder: Folder, cursor: Int?, pageSize: Long): List<Feed>
}

