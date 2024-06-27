package com.jordyma.blink.auth.dto.response

import lombok.AccessLevel
import lombok.Getter
import lombok.NoArgsConstructor


data class TokenResponseDto (
//    @Schema(description = "액세스 토큰")
    val accessToken: String,

//    @Schema(description = "리프레시 토큰")
    val refreshToken: String,
    )