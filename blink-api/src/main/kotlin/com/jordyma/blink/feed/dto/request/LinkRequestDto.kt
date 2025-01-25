package com.jordyma.blink.feed.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "텍스트 임시 전달 dto - 파서 배포 전까지 사용")
data class LinkRequestDto (

    @Schema(description = "링크 url")
    val link: String,
)