package com.jordyma.blink.global.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate


@Configuration
class GeminiRestTemplateConfig {

    @Bean
    @Qualifier("geminiRestTemplate")
    fun geminiRestTemplate(): RestTemplate {
        val restTemplate = RestTemplate()
        restTemplate.interceptors.add { request, body, execution -> execution.execute(request, body) }

        return restTemplate
    }
}