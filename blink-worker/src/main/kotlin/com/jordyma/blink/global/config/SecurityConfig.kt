//package com.jordyma.blink.global.config
//
//import com.jordyma.blink.global.filter.JwtAuthenticationFilter
//import com.jordyma.blink.global.filter.JwtExceptionFilter
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.authentication.AuthenticationManager
//import org.springframework.security.config.Customizer
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.http.SessionCreationPolicy
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
//
//@Configuration
//class SecurityConfig(private val authenticationManager: AuthenticationManager) {
//
//        // TODO 예외 처리 추가하고
//        // swaggerConfig에 인증 버튼 추가
//
//    val permitAllUrls = arrayOf<String>(
//            "/api-docs-ui",
//            "/swagger-ui/**",
//            "/swagger-resources/**",
//            "/v3/api-docs/**",
//            "/v3/api-docs",
//            "/location/**",
//            "/auth/kakao-login",
//            "/auth/kakao-login-web/callback",
//            "/api/**",
//            "/error",
//            // 다른 기능 완성되기 전 임시로 모두 허용
//            "/**"
//    )
//
//    @Bean
//    fun filterChain(http: HttpSecurity) = http
//            .httpBasic { it.disable() }
//            .formLogin { it.disable() }
//            .cors(Customizer.withDefaults())
//            .csrf { it.disable()}
//            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
//            .authorizeHttpRequests { it.requestMatchers(*permitAllUrls).permitAll().anyRequest().authenticated() }
//            .addFilterBefore(JwtAuthenticationFilter(authenticationManager, permitAllUrls), UsernamePasswordAuthenticationFilter::class.java)
//            .addFilterBefore(JwtExceptionFilter(), JwtAuthenticationFilter::class.java)
//            .build()
//}