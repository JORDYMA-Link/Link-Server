package com.jordyma.blink.feed.vo

import com.jordyma.blink.feed.Feed
import com.jordyma.blink.keyword.Keyword

data class FeedKeywordVo(
    val feed: Feed,
    val keywords: List<Keyword>
)
