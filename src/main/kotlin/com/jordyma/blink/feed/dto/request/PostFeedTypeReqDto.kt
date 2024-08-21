package com.jordyma.blink.feed.dto.request

import com.jordyma.blink.feed.dto.FeedType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty

@Schema(description = "홈화면 북마크(중요)/미분류 피드 리스트 요청 dto")
data class PostFeedTypeReqDto(
    @field:NotEmpty(message = "type은 필수 입력 값입니다.")
    @Schema(description = "BOOKMARKED / UNCLASSIFIED")
    val type: FeedType,

    @field:Min(value = 0, message = "page는 0 이상의 값이어야 합니다.")
    @Schema(description = "page (0부터 시작)")
    val page: Int = 0,

    @field:Min(value = 1, message = "size는 1 이상의 값이어야 합니다.")
    @Schema(description = "size(default = 10)")
    val size: Int = 10
)

