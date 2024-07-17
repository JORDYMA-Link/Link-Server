package com.jordyma.blink.feed.repository

import com.jordyma.blink.feed.vo.FeedDetailVo
import com.jordyma.blink.feed.vo.FeedFolderVo
import java.time.LocalDateTime

interface CustomFeedRepository {
    fun findFeedFolderDtoByUserIdAndBetweenDate(userId: Long, startOfMonth: LocalDateTime, endOfMonth: LocalDateTime): List<FeedFolderVo>

    fun findFeedDetail(memberId: Long, feedId: Long): FeedDetailVo?
}
