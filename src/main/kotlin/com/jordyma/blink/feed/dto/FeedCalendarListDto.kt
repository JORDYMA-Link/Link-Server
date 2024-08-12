package com.jordyma.blink.feed.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "캘린더 피드 리스트 DTO (list 비어있으면 isArchived가 false)")
data class FeedCalendarListDto(
    val isArchived: Boolean,
    val list: List<FeedDto>
)