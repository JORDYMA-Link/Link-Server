package com.jordyma.blink.feed.domain.service

import com.jordyma.blink.feed.domain.Feed
import com.jordyma.blink.feed.domain.Source

interface FeedSummarizeService {
    fun updateSummarizedFeed(
        content: SummaryContent,
        brunch: Source,
        feedId: Long,
        userId: Long,
        thumbnailImage: String,
    ): Feed

    fun createRecommendFolders(feed: Feed, category: List<String>)
}