package com.jordyma.blink.feed.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "요약 저장 요청 dto")
data class FeedCreateReqDto (

    @Schema(description = "링크 url")
    val url: String,

    @Schema(description = "폴더 이름")
    val folderName: String,

    @Schema(description = "제목")
    val title: String,

    @Schema(description = "요약내용")
    val summary: String,

    @Schema(description = "키워드")
    val keywords: List<String>,

    @Schema(name = "플랫폼 이미지 url")
    val sourceImage: String,

    @Schema(name = "메모")
    var memo: String,
)