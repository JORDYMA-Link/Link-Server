package com.jordyma.blink.feed.domain

import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface FeedRepository : JpaRepository<Feed, Long>, FeedRepositoryCustom {

}

