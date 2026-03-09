package com.jordyma.blink.global.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

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
        
        config.lockWatchdogTimeout = 30000L
        
        return Redisson.create(config)
    }
}
