package com.jordyma.blink.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "유저 프로필 수정 요청 DTO")
data class UpdateUserProfileReqDto(

    @Schema(description = "닉네임")
    val nickname: String,
)