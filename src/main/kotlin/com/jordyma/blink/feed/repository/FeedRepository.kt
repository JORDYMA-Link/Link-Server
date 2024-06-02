package com.jordyma.blink.feed.repository

import com.jordyma.blink.feed.entity.Feed
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface FeedRepository : JpaRepository<Feed, Long>, CustomFeedRepository {

}

