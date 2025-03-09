package com.jordyma.blink.notice.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "웹뷰 dto")
data class WebViewDto(

    @Schema(description = "표시여부")
    val flag: Boolean,

    @Schema(description = "url")
    val link: String,
)