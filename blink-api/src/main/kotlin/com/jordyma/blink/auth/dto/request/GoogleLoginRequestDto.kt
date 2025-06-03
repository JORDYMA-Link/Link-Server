package com.jordyma.blink.auth.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

data class GoogleLoginRequestDto (
    @NotNull
    @Schema(description = "idToken")
    val idToken: String,
)