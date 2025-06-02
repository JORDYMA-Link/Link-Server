package com.jordyma.blink.auth.api

import com.jordyma.blink.auth.dto.response.GoogleUserResponseDto
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.PostExchange

interface GoogleAuthApi {
    @GetExchange("/tokeninfo")
    fun getGoogleUserInfo(@RequestParam("id_token") idToken: String): GoogleUserResponseDto
}
