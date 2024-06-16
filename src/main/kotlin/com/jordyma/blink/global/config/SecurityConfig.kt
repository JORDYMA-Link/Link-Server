package com.jordyma.blink.global.config

import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain


@Configuration
class SecurityConfig() {
    val permitAllUrls = arrayOf<String>(
        "/swagger-ui/**",
        "/swagger-resources/**",
        "/location/**",
        "/api/auth/kakao-login",
        "/api/**",
        "/error",
        "/test")

    @Bean
    fun filterChain(http: HttpSecurity) = http
        .httpBasic { it.disable() }
        .formLogin { it.disable() }
        .cors(Customizer.withDefaults())
        .csrf { it.disable()}
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        .authorizeHttpRequests { it.requestMatchers(*permitAllUrls).permitAll().anyRequest().authenticated() }
        .build()

}