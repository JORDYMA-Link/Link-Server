package com.jordyma.blink.user.dto.request

import com.jordyma.blink.user.constants.PushTokenType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "유저 푸시토큰 갱신 요청 DTO")
data class UpdateUserPushTokenRequestDto (

    @Schema(description = "푸시토큰 타입(AOS, IOS)")
    val pushTokenType: PushTokenType,

    @Schema(description = "푸시토큰")
    val pushToken: String
)