package com.jordyma.blink.keyword

import org.springframework.data.jpa.repository.JpaRepository

interface KeywordRepository : JpaRepository<Keyword, Long> {
    fun findByFeedId(feedId: Long): List<Keyword>
}