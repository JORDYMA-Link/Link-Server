package com.jordyma.blink.feed.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "피드 아이템 DTO")
data class FeedDto(
    @Schema(description = "폴더 ID")
    val folderId: Long,

    @Schema(description = "폴더 이름")
    val folderName: String,

    @Schema(description = "피드 ID")
    val feedId: Long,

    @Schema(description = "피드 제목")
    val title: String,

    @Schema(description = "피드 요약")
    val summary: String,

    @Schema(description = "원본 플랫폼 URL")
    val platform: String,

    @Schema(description = "피드 원본 URL")
    val sourceUrl: String,

    @Schema(description = "북마크 여부")
    val isMarked: Boolean,

    @Schema(description = "키워드 리스트")
    val keywords: List<String>
)