package com.jordyma.blink.feed.domain.service

interface PageParser {

    fun parseUrl(url: String): PageInfo
}

data class PageInfo(
    val title: String,
    val content: String,
    val thumbnailImage: String,
)