package com.jordyma.blink.auth.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

data class GoogleCallbackRequestDto (
    val clientId: String? = null,
    val clientSecret: String? = null,
    val code: String? = null,
    val grantType: String? = "authorization_code",
    val redirectUri: String? = null,
)