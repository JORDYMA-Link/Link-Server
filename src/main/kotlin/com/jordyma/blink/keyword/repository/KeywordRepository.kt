package com.jordyma.blink.keyword.repository

import com.jordyma.blink.keyword.entity.Keyword
import org.springframework.data.jpa.repository.JpaRepository

interface KeywordRepository : JpaRepository<Keyword, Long> {
    fun findByFeedId(feedId: Long): List<Keyword>
}