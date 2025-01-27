package com.jordyma.blink.feed.repository

import com.jordyma.blink.feed.Feed
import org.springframework.data.jpa.repository.JpaRepository

interface FeedRepository : JpaRepository<Feed, Long>, FeedRepositoryCustom {

}

