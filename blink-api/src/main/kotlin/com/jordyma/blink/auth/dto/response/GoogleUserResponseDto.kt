package com.jordyma.blink.auth.dto.response

data class GoogleUserResponseDto(
    val sub: String,       // 구글 사용자 고유 ID
    val email: String,
    val name: String
)
