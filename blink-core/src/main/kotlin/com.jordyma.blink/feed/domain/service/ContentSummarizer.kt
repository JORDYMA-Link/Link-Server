package com.jordyma.blink.feed.domain.service

import kotlinx.serialization.Serializable

interface ContentSummarizer {
    fun summarize(
        content: String,
        link: String,
        folders: String,
        userId: Long,
        feedId: Long
    ): PromptResponse
}
@Serializable
data class PromptResponse(
    val subject: String,
    val summary: String,
    val keyword: List<String>,
    val category: List<String>,
)