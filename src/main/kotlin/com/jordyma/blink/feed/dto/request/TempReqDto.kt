package com.jordyma.blink.feed.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "텍스트 임시 전달 dto - 파서 배포 전까지 사용")
data class TempReqDto (

    @Schema(description = "링크 url")
    val link: String,

    // TODO: 파서 배포 후 삭제
    @Schema(description = "본문 내용")
    val content: String,
)