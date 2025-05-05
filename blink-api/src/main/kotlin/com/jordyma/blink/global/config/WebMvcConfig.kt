package com.jordyma.blink.global.config

import com.jordyma.blink.infra.UserActivityInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig : WebMvcConfigurer {

    @Autowired
    private lateinit var userActivityInterceptor: UserActivityInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
//        registry.addInterceptor(userActivityInterceptor)
//            .addPathPatterns("/api/**", "/user/**", "/auth/**")
    }
}