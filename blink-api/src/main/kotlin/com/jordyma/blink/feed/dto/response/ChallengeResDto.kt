package com.jordyma.blink.feed.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "프로모션 챌린지 DTO")
data class ChallengeResDto(

    @Schema(description = "모달 표시 여부")
     val isVisible: Boolean,

    @Schema(description = "단계")
    val count: Int,
)