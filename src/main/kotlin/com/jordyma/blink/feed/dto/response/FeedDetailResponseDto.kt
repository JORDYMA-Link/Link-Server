package com.jordyma.blink.feed.dto.response

import io.swagger.v3.oas.annotations.media.Schema


@Schema(description = "피드 상세 DTO")
data class FeedDetailResponseDto(
    @Schema(description = "피드 ID")
    val feedId: Long,

    @Schema(description = "썸네일 배경 이미지 URL")
    val thumnailImage: String?,

    @Schema(description = "플랫폼 이미지 URL")
    val platformImage: String,

    @Schema(description = "피드 제목")
    val title: String,

    @Schema(description = "생성 날짜 (yyyy-MM-dd 날짜 형식)")
    val date: String,

    @Schema(description = "피드 요약 내용")
    val summary: String,

    @Schema(description = "keyword 리스트(3개)")
    val keywords: List<String>,

    @Schema(description = "피드가 속한 폴더 이름(1개)")
    val folderName: String,

    @Schema(description = "메모 (내용 없으면 ''으로 반환, 공백 포함 최대 1000자)")
    val memo: String,

    @Schema(description = "북마크 여부")
    val isMarked: Boolean,

    @Schema(description = "원본 주소 URL (공유하기에서 사용)")
    val originUrl: String,

    @Schema(description = "플랫폼")
    val platform: String,

    @Schema(description = "미분류 여부")
    val isUnclassified: Boolean,

    @Schema(description = "추천 폴더 리스트 (미분류 대상에만 존재, 이외는 Null)")
    val recommendedFolder: List<String>? = null,

    @Schema(description = "폴더 id")
    val folderId: Long,

    )