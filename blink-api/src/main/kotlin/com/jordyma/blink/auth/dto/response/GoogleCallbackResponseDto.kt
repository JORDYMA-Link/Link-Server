package com.jordyma.blink.auth.dto.response

data class GoogleCallbackResponseDto(
    val access_token: String,
    val expires_in: Int,
    val refresh_token: String?,
    val scope: String,
    val token_type: String,
    val id_token: String    // Google에서 디지털 서명한 사용자의 id JWT 토큰
)