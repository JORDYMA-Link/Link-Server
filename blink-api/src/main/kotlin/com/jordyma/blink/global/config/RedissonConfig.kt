package com.jordyma.blink.global.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Redisson 분산락 설정 (API 서버용)
 * - V2 Executor의 중복 요약 방지
 * - Watchdog을 통한 자동 TTL 연장
 */
@Configuration
class RedissonConfig(
    @Value("\${spring.data.redis.host}")
    private val host: String,

    @Value("\${spring.data.redis.port}")
    private val port: Int,

    @Value("\${spring.data.redis.password:}")
    private val password: String
) {

    @Bean
    fun redissonClient(): RedissonClient {
        val config = Config()
        
        val address = "redis://$host:$port"
        config.useSingleServer()
            .setAddress(address)
            .setConnectionPoolSize(64)
            .setConnectionMinimumIdleSize(10)
            .setDnsMonitoringInterval(-1)
        
        if (password.isNotEmpty()) {
            config.useSingleServer().setPassword(password)
        }
        
        // Watchdog 타임아웃: 30초마다 자동으로 락 TTL 연장
        config.lockWatchdogTimeout = 30000L
        
        return Redisson.create(config)
    }
}
