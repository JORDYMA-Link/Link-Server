package com.jordyma.blink.feed.domain.service

import com.jordyma.blink.user.LanguageType
import kotlinx.serialization.Serializable

interface ContentSummarizer {
    fun summarize(
        content: String,
        link: String,
        folders: String,
        userId: Long,
        feedId: Long,
        language: LanguageType? = LanguageType.KOREAN,
    ): PromptResponse
}
@Serializable
data class PromptResponse(
    val subject: String,
    val summary: String,
    val keyword: List<String>,
    val category: List<String>,
)

@Serializable
data class PromptSummaryResponse(
    val subject: String,
    val summary: String,
)

@Serializable
data class PromptMetadataResponse(
    val keyword: List<String>,
    val category: List<String>,
)