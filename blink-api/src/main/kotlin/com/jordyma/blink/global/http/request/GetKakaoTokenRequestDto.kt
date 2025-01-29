package com.jordyma.blink.global.http.request

data class GetKakaoTokenRequestDto(
    val grant_type: String = "authorization_code",
    val client_id: String,
    val redirect_uri: String,
    val code: String
)
