package com.jordyma.blink.global.http.api

import com.jordyma.blink.global.http.response.GetKakaoTokenResponseDto
import com.jordyma.blink.global.http.response.OpenKeyListResponse
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.PostExchange


interface KakaoAuthApi {
    @GetExchange("/.well-known/jwks.json")
    fun getKakaoOpenKeyAddress(): OpenKeyListResponse

    @PostExchange("/oauth/token", contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    fun getKakaoToken(
        @RequestParam client_id: String, @RequestParam redirect_uri: String, @RequestParam code: String,
        @RequestParam grant_type: String = "authorization_code",
    ): GetKakaoTokenResponseDto
}