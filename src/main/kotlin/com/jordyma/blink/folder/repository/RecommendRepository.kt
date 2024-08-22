package com.jordyma.blink.folder.repository

import com.jordyma.blink.folder.entity.Recommend
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface RecommendRepository : JpaRepository<Recommend, Long> {
    @Query("SELECT r FROM Recommend r WHERE r.feed.id = :feedId")
    fun findRecommendationsByFeedId(feedId: Long): List<Recommend>
}