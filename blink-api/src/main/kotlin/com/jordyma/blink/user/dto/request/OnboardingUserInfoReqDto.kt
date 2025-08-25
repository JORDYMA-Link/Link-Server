package com.jordyma.blink.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "온보딩 유저 정보 입력 요청 DTO")
data class OnboardingUserInfoReqDto(

    @Schema(description = "직무 분야")
    val jobField : String,

    @Schema(description = "연령대")
    val birthYear : String,

    @Schema(description = "성별")
    val gender : String,
)
