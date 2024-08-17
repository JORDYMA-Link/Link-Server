package com.jordyma.blink.feed.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "요약 저장(수정) 응답 dto")
data class FeedUpdateResDto(
    val id: Long,
)