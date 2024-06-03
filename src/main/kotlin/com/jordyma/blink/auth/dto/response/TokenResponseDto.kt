package com.jordyma.blink.auth.dto.response

import lombok.AccessLevel
import lombok.Getter
import lombok.NoArgsConstructor


data class TokenResponseDto (
//    @Schema(description = "액세스 토큰")
    val accessToken: String,

//    @Schema(description = "리프레시 토큰")
    val refreshToken: String,

//    companion object {
//        fun of(accessToken: String?, refreshToken: String?): TokenResponseDto {
//            val tokenResponseDto = TokenResponseDto()
//            tokenResponseDto.accessToken = accessToken
//            tokenResponseDto.refreshToken = refreshToken
//
//            return tokenResponseDto
//        }
//    }
    )