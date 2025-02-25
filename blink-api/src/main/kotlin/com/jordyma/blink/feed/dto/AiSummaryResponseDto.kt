package com.jordyma.blink.feed.dto

import com.jordyma.blink.infra.gemini.response.PromptResponse
import io.swagger.v3.oas.annotations.media.Schema
import lombok.NoArgsConstructor
import java.time.LocalDate

@NoArgsConstructor
@Schema(description = "AI 요약 결과 내용 DTO")
data class AiSummaryContent(
    @Schema(description = "제목")
    val subject: String,

    @Schema(description = "요약 내용")
    val summary: String,

    @Schema(description = "키워드")
    val keywords: List<String>,

    @Schema(description = "유저가 생성한 폴더 리스트")
    val folders: List<String>
) {
    companion object {
        fun from(promptResponse: PromptResponse) = AiSummaryContent(
            subject = promptResponse.subject ?: "",
            summary = promptResponse.summary ?: "",
            keywords = promptResponse.keyword ?: emptyList(),
            folders = promptResponse.category ?: emptyList(),
        )
    }
}

@Schema(description = "AI 요약 결과 DTO")
data class AiSummaryResponseDto(

    @Schema(description = "피드 id")
    val feedId: Long,

    @Schema(description = "제목")
    val subject: String,

    @Schema(description = "요약 내용")
    val summary: String,

    @Schema(description = "키워드")
    val keywords: List<String>,

    @Schema(description = "유저가 생성한 폴더 리스트")
    val folders: List<String>,

    @Schema(description = "플랫폼 이미지 url")
    val platformImage: String,

    @Schema(description = "썸네일 이미지 url")
    val thumbnailImage: String? = null,

    @Schema(description = "AI 추천 폴더")
    val recommendFolder: String,

    @Schema(description = "AI 추천 폴더 리스트 (링크 저장 api request에 사용)")
    val recommendFolders: List<String>,

    @Schema(description = "요약 날짜")
    val date: LocalDate
)

@Schema(description = "피드 id response DTO")
data class FeedIdResponseDto(

    @Schema(description = "피드 id")
    val feedId: Long,
)