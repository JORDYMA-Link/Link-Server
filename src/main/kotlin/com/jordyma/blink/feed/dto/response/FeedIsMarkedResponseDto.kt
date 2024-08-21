package com.jordyma.blink.feed.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "피드 북마킹 성공 여부 응답 DTO")
data class FeedIsMarkedResponseDto(
    @Schema(description = "피드 ID")
    val id: Long,

    @Schema(description = "북마킹 여부")
    val isMarked: Boolean,

    @Schema(description = "수정일 타임스탬프")
    val modifiedDate: String
)
