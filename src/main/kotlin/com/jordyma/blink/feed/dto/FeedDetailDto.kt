package com.jordyma.blink.feed.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "피드 상세 DTO")
data class FeedDetailDto(
    val feedId: Long,
    val thumnailImage: String,
    val title: String,
    val date: String, @Schema(description = "yyyy-MM-dd 날짜 형식으로 반환됩니다.")
    val summary: String,
    val keywords: List<String>,
    val folderName: String,
    val memo: String
)
