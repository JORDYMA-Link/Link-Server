package com.jordyma.blink.feed.repository

import com.jordyma.blink.feed.entity.Feed
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface FeedRepository : JpaRepository<Feed, Long> {

    @Query("SELECT f FROM Feed f WHERE DATE(f.createdAt) = :date")
    fun findByCreatedAt(@Param("date") date: LocalDate): List<Feed>
}
