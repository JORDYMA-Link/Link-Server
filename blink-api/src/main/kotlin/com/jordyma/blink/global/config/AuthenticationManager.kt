package com.jordyma.blink.global.config

import com.jordyma.blink.auth.jwt.authentication_provider.JwtAuthenticationProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager

@Configuration
class AuthenticationManagerConfig(private val jwtAuthenticationProvider: JwtAuthenticationProvider) {

    /*
     * AuthenticationManager를 주입받기 위해서 빈으로 등록한다.
     * */
    @Bean
    @Throws(Exception::class)
    fun authenticationManager(): AuthenticationManager {
        return ProviderManager(jwtAuthenticationProvider)
    }
}