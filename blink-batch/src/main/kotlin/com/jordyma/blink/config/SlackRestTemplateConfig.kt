package com.jordyma.blink.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate


@Configuration
class SlackRestTemplateConfig {

    @Bean(name = ["slackRestTemplate"])
    fun slackRestTemplate(): RestTemplate {
        return RestTemplate()
    }
}