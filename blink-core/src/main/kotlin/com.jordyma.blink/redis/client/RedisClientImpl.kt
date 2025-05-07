package com.jordyma.blink.redis.client

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.script.RedisScript
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

    override fun incr(key: String): Long? {
        return redisTemplate.opsForValue().increment(key)
    }

    override fun sadd(key: String, value: String): Long? {
        return redisTemplate.opsForSet().add(key, value)
    }

    override fun scard(key: String): Long? {
        return redisTemplate.opsForSet().size(key)
    }

    override fun expire(key: String, seconds: Long): Boolean {
        return redisTemplate.expire(key, seconds, java.util.concurrent.TimeUnit.SECONDS) ?: false
    }

    // 스크립트 실행
    override fun eval(script: String, keys: List<String>, args: List<String>): Any? {
        return redisTemplate.execute(RedisScript.of(script, Any::class.java), keys, *args.toTypedArray())
    }

}