package com.jordyma.blink.feed.repository

import com.jordyma.blink.feed.vo.FeedFolderVo
import java.time.LocalDate

interface CustomFeedRepository {
    fun findFeedFolderDtoByUserAndDate(userId: Long, date: LocalDate): List<FeedFolderVo>
}