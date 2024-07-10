package com.jordyma.blink.feed.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

@Schema(description = "캘린더 피드 리스트 DTO (yyyy-MM: FeedCalendarListDto)")
data class FeedCalendarResponseDto(
    val monthlyFeedList: Map<String, FeedCalendarListDto>
)

@Schema(description = "캘린더 피드 리스트 DTO (list 비어있으면 isArchived가 false)")
data class FeedCalendarListDto(
    val isArchived: Boolean,
    val list: List<FeedItemDto>
)

@Schema(description = "피드 아이템 DTO")
data class FeedItemDto(
    val folderId: Long,
    val folderName: String,
    val feedId: Long,
    val title: String,
    val summary: String,
    val source: String,
    val sourceUrl: String,
    val isMarked: Boolean,
    val keywords: List<String>
)