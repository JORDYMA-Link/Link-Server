package com.jordyma.blink.global.config

import com.jordyma.blink.common.logger.BlinkLogger
import com.jordyma.blink.common.logger.Slf4jBlinkLogger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LoggerConfig {
    @Bean
    fun blinkLogger(): BlinkLogger = Slf4jBlinkLogger()
}