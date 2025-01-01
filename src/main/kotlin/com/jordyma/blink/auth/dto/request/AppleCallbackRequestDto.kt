package com.jordyma.blink.auth.dto.request

data class AppleCallbackRequestDto (
    val code: String,
    val idToken: String,
    val state: String? = null
)