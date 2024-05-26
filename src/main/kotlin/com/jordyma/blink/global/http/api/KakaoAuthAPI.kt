package com.jordyma.blink.global.http.api

import com.jordyma.blink.global.http.response.OpenKeyListResponse
import org.springframework.web.service.annotation.GetExchange


interface KakaoAuthApi {
    @GetExchange("/.well-known/jwks.json")
    fun getKakaoOpenKeyAddress(): OpenKeyListResponse
}