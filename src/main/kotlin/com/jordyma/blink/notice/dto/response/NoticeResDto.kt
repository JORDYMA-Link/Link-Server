package com.jordyma.blink.notice.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "공지사항 DTO")
data class NoticeResDto (

    @Schema(description = "날짜")
    val date: String,

    @Schema(description = "제목")
    val title: String,

    @Schema(description = "내용")
    val content: String,
)