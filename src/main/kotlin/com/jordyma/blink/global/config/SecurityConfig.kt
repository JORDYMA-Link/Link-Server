package com.jordyma.blink.global.config

import com.jordyma.blink.global.filter.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(private val authenticationManager: AuthenticationManager) {


    val permitAllUrls = arrayOf<String>(
            "/api-docs-ui",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/v3/api-docs",
            "/location/**",
            "/auth/kakao-login",
            "/auth/kakao-login-web/callback",
            "/error")

    @Bean
    fun filterChain(http: HttpSecurity) = http
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .cors(Customizer.withDefaults())
            .csrf { it.disable()}
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { it.requestMatchers(*permitAllUrls).permitAll().anyRequest().authenticated() }
            .addFilterBefore(JwtAuthenticationFilter(authenticationManager, permitAllUrls), UsernamePasswordAuthenticationFilter::class.java)
            .build()
}