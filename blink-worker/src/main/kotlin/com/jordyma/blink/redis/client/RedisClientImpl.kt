package com.jordyma.blink.redis.client

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class RedisClientImpl(private val redisTemplate: RedisTemplate<String, String>) : RedisClient {

    override fun set(key: String, value: String) {
        redisTemplate.opsForValue().set(key, value)
    }

    override fun get(key: String): String {
        return redisTemplate.opsForValue().get(key) ?: ""
    }

    override fun decr(key: String): Long? {
        return redisTemplate.opsForValue().decrement(key)
    }

}