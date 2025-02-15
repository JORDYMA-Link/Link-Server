package com.jordyma.blink.feed.domain

import org.springframework.data.jpa.repository.JpaRepository

interface FeedRepository : JpaRepository<Feed, Long>, FeedRepositoryCustom {

}

