package com.jordyma.blink.feed_summarizer.request_limiter

import com.jordyma.blink.redis.client.RedisClient
import com.jordyma.blink.redis.constants.RedisKey
import org.springframework.stereotype.Component

@Component
class SummarizeRequestLimiterImpl(
    private val redisClient: RedisClient
): SummarizeRequestLimiter {

    val REFILL_TOKEN_COUNT = 15

    override fun decreaseToken(): Long {
        val remainCount = this.redisClient.decr(RedisKey.SUMMARIZE_REQUEST_TOKEN_COUNT.name).let { it ?: 0 }

        return remainCount
    }

    override fun refillToken() {
        this.redisClient.set(RedisKey.SUMMARIZE_REQUEST_TOKEN_COUNT.name, REFILL_TOKEN_COUNT.toString())
    }
}