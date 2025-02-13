package com.jordyma.blink.feed.domain.service

interface ContentSummarizer {
    fun summarize(
        content: String,
        link: String,
        folders: String,
        userId: Long,
        feedId: Long
    ): SummaryContent
}

data class SummaryContent(
    val subject: String,
    val summary: String,
    val keywords: List<String>,
    val categories: List<String>
)