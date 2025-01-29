package com.jordyma.blink.notice.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "공지사항 dto")
data class NoticeResDto (

    @Schema(description = "날짜")
    val date: String,

    @Schema(description = "제목")
    val title: String,

    @Schema(description = "내용")
    val content: String,
)

@Schema(description = "공지사항 리스트 dto")
data class NoticeListDto(

    @Schema(description = "공지사항 리스트")
    val notices: List<NoticeResDto>
)
