package com.jordyma.blink.feed.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

@Schema(description = "캘린더 피드 리스트 DTO ")
data class FeedCalendarResponseDto(
    @Schema(description = "yyyy-MM-dd를 key로 FeedCalendarListDto를 value로 가지는 맵")
    val monthlyFeedMap: Map<String, FeedCalendarListDto>
)

@Schema(description = "캘린더 피드 리스트 DTO")
data class FeedCalendarListDto(
    @Schema(description = "아카이브(저장)된 데이터의 존재 여부(list 비어있으면 isArchived가 false)")
    val isArchived: Boolean,

    @Schema(description = "피드 아이템 리스트")
    val list: List<FeedItemDto>
)

@Schema(description = "피드 아이템 DTO")
data class FeedItemDto(
    @Schema(description = "폴더 ID")
    val folderId: Long,

    @Schema(description = "폴더 이름")
    val folderName: String,

    @Schema(description = "피드 ID (상세 피드 조회시 사용)")
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

    @Schema(description = "키워드 리스트(3개)")
    val keywords: List<String>
)