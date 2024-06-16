package com.jordyma.blink.global.http.api

import com.jordyma.blink.global.http.request.GetKakaoTokenRequestDto
import com.jordyma.blink.global.http.response.GetKakaoTokenResponseDto
import com.jordyma.blink.global.http.response.OpenKeyListResponse
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.PostExchange


interface KakaoAuthApi {
    @GetExchange("/.well-known/jwks.json")
    fun getKakaoOpenKeyAddress(): OpenKeyListResponse

    @PostExchange("/oauth/token")
    fun getKakaoToken(@RequestBody getKakaoTokenRequestDto: GetKakaoTokenRequestDto): GetKakaoTokenResponseDto
}