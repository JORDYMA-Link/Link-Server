package com.jordyma.blink.auth.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

data class KakaoLoginRequestDto (
    @NotNull
    @Schema(description = "idToken")
    val idToken: String,

    @NotNull
    @Schema(description = "nonce")
    val nonce: String,
)