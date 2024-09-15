package com.jordyma.blink.feed.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "피드 중요/미분류 피드 리스트(홈) 응답 DTO")
data class FeedTypeResponseDto(
    val feedList : List<FeedTypeDto>
)

@Schema(description = "피드 중요/미분류 피드 리스트(홈) DTO")
data class FeedTypeDto(
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

    @Schema(description = "미분류 여부")
    val isUnclassified: Boolean,

    @Schema(description = "피드 키워드 리스트(3개)")
    val keywords: List<String>,

    @Schema(description = "추천 폴더 리스트 (미분류 대상에만 존재, 이외는 Null)")
    val recommendedFolder: List<String>? = null,

    @Schema(description = "폴더 id")
    val folderId: Long,

    @Schema(description = "폴더 이름")
    val folderName: String,
)