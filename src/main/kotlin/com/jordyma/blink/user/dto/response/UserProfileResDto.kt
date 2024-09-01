package com.jordyma.blink.user.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "유저 프로필 조회 DTO")
data class UserProfileResDto (

    @Schema(description = "닉네임")
    val nickName: String,

//    @Schema(description = "이메일")
//    val email: String,
)