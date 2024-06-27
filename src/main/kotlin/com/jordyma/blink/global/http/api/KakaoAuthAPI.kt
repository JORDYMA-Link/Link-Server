package com.jordyma.blink.global.http.api

import com.jordyma.blink.global.http.request.GetKakaoTokenRequestDto
import com.jordyma.blink.global.http.response.GetKakaoTokenResponseDto
import com.jordyma.blink.global.http.response.OpenKeyListResponse
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.PostExchange


interface KakaoAuthApi {
    @GetExchange("/.well-known/jwks.json")
    fun getKakaoOpenKeyAddress(): OpenKeyListResponse

    @PostExchange("/oauth/token", contentType ="application/x-www-form-urlencoded;charset=utf-8")
    fun getKakaoToken(
        @RequestParam client_id: String, @RequestParam redirect_uri: String, @RequestParam code: String
    ): GetKakaoTokenResponseDto
}