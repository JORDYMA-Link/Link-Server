package com.jordyma.blink.auth.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import lombok.AccessLevel
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor


//@Getter
//@AllArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PRIVATE)
data class KakaoLoginRequestDto (
    @NotNull
    @Schema(description = "idToken")
    val idToken: String,

    @NotNull
    @Schema(description = "nonce")
    val nonce: String,
)