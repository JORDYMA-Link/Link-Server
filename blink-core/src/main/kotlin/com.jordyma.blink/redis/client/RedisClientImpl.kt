package com.jordyma.blink.redis.client

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(
    name = ["spring.data.redis.enabled"],
    havingValue = "true",
    matchIfMissing = false
)
class RedisClientImpl(private val redisTemplate: RedisTemplate<String, String>) : RedisClient {

    override fun set(key: String, value: String) {
        redisTemplate.opsForValue().set(key, value)
    }

    override fun set(key: String, value: String, ttlSeconds: Long) {
        redisTemplate.opsForValue().set(key, value, ttlSeconds, java.util.concurrent.TimeUnit.SECONDS)
    }

    override fun get(key: String): String {
        return redisTemplate.opsForValue().get(key) ?: ""
    }

    override fun del(key: String): Boolean {
        return redisTemplate.delete(key)
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

    override fun zcount(key: String, min: Long, max: Long): Int {
        return redisTemplate.opsForZSet().count(key, min.toDouble(), max.toDouble())?.toInt() ?: 0
    }

    override fun zadd(key: String, score: Double, member: String): Long {
        val added = redisTemplate.opsForZSet().add(key, member, score)
        return if (added == true) 1L else 0L
    }

    override fun zremrangebyscore(key: String, min: Double, max: Double): Long {
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max) ?: 0L
    }

}