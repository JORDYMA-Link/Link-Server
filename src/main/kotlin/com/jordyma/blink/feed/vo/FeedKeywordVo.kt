package com.jordyma.blink.feed.vo

import com.jordyma.blink.feed.entity.Feed
import com.jordyma.blink.keyword.entity.Keyword

data class FeedKeywordVo(
    val feed: Feed,
    val keywords: List<Keyword>
)
