package com.jordyma.blink.feed.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "피드 검색 응답 DTO")
data class FeedSearchResponseDto(
    @Schema(description = "검색어")
    val query: String,

    @Schema(description = "검색 결과 피드 리스트")
    val result: List<FeedResultDto>
)

@Schema(description = "피드 검색 결과 DTO")
data class FeedResultDto(
    @Schema(description = "피드 ID")
    val feedId: Long,

    @Schema(description = "피드 제목")
    val title: String,

    @Schema(description = "피드 요약 내용")
    val summary: String,

    @Schema(description = "플랫폼(유튜브, 네이버 등)")
    val platform: String,

    @Schema(description = "플랫폼 로고 이미지 URL")
    val platformImage: String,

    @Schema(description = "북마크 여부")
    val isMarked: Boolean,

    @Schema(description = "키워드 리스트")
    val keywords: List<String>
)
