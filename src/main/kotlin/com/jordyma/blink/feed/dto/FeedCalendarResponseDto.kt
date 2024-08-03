package com.jordyma.blink.feed.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

@Schema(description = "캘린더 피드 리스트 DTO (yyyy-MM: FeedCalendarListDto)")
data class FeedCalendarResponseDto(
    val monthlyFeedList: Map<String, FeedCalendarListDto>
)



