package com.jordyma.blink.infra.ratelimit

import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.logger
import com.jordyma.blink.redis.client.RedisClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.lang.Math.ceil
import java.time.Instant

@Component
class SummarizeLimiter(
    private val redisClient: RedisClient,
    @Value("\${rate.limit.window-size}")
    private val windowSizeSec: Int,
    @Value("\${rate.limit.max-requests}")
    private val maxRequests: Int,
) {
    // 요청을 받을 수 있는지 계산
    fun isAllowed(key: String): Boolean {
        val now = Instant.now().epochSecond
        val currentWindowStart = now - windowSizeSec
        val previousWindowStart = currentWindowStart - windowSizeSec

        val currentWindowCount = getCurrentWindowCount(key, currentWindowStart, now)

        val previousWindowCount = getPreviousWindowCount(key, previousWindowStart, currentWindowStart)

        val overlapRatio = calculateOverlapRatio(now)

        // Moving Window Counter
        val estimatedCount = currentWindowCount + (previousWindowCount * overlapRatio)
        val finalCount = ceil(estimatedCount).toInt()

        logger().info("Moving Window : current=${currentWindowCount}, previous=${previousWindowCount}, " +
                "overlapRatio=${overlapRatio}, estimated=${estimatedCount}, final=${finalCount}")

        if (finalCount >= maxRequests) {
            return false
        }

        recordCurrentRequest(key, now)

        return true
    }

    private fun getCurrentWindowCount(key: String, windowStart: Long, windowEnd: Long): Int {
        return redisClient.zcount(key, windowStart, windowEnd)
    }

    private fun getPreviousWindowCount(key: String, windowStart: Long, windowEnd: Long): Int {
        return redisClient.zcount(key, windowStart, windowEnd)
    }

    private fun calculateOverlapRatio(currentTime: Long): Double {
        val secondsIntoCurrentWindow = currentTime % windowSizeSec
        val remainingFromPreviousWindow = windowSizeSec - secondsIntoCurrentWindow
        return remainingFromPreviousWindow.toDouble() / windowSizeSec
    }

    private fun recordCurrentRequest(key: String, timestamp: Long) {
        redisClient.zadd(key, timestamp.toDouble(), timestamp.toString())
        redisClient.expire(key, (windowSizeSec * 2).toLong())

        // 2 윈도우(2분)보다 오래된 데이터 정리
        val cleanupThreshold = timestamp - (windowSizeSec * 2)
        redisClient.zremrangebyscore(key, 0.0, cleanupThreshold.toDouble())
    }
}
