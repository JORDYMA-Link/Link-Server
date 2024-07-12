package com.jordyma.blink.feed.repository

import com.jordyma.blink.feed.vo.FeedFolderVo
import java.time.LocalDate
import java.time.LocalDateTime

interface CustomFeedRepository {
    fun findFeedFolderDtoByUserIdAndBetweenDate(userId: Long, startOfMonth: LocalDateTime, endOfMonth: LocalDateTime): List<FeedFolderVo>
}