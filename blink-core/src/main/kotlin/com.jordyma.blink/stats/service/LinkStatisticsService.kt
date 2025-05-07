package com.jordyma.blink.stats.service

import com.jordyma.blink.redis.client.RedisClient
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class LinkStatisticsService(private val redisClient: RedisClient) {

    // INCR 한 뒤 '첫 생성'이면 TTL 부여
    private val incrExpireLua = """
        local val = redis.call('INCR', KEYS[1])
        if tonumber(val) == 1 then
            redis.call('EXPIRE', KEYS[1], ARGV[1])
        end
        return val
    """.trimIndent()

    // SADD 후 '첫 생성'이면 TTL 부여
    private val saddExpireLua = """
        local added = redis.call('SADD', KEYS[1], ARGV[1])
        if added == 1 and redis.call('SCARD', KEYS[1]) == 1 then
            redis.call('EXPIRE', KEYS[1], ARGV[2])
        end
        return added
    """.trimIndent()

    // TTL 3일
    private val ttlSeconds = (60 * 60 * 24 * 3).toString()


    fun getYesterdayLinkViewCount(): Int {
        val yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_DATE)
        return redisClient.get("$LINK_VIEW_COUNT_KEY_PREFIX$yesterday")?.toIntOrNull() ?: 0
    }

    fun getYesterdayDailyActiveUsers(): Int {
        val yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_DATE)
        return redisClient.scard("$DAILY_ACTIVE_USERS_KEY_PREFIX$yesterday")?.toInt() ?: 0
    }

    // 링크 조회 api 호출시 증가
    fun incrementLinkView() {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        val key = "$LINK_VIEW_COUNT_KEY_PREFIX$today"
        redisClient.eval(incrExpireLua, listOf(key), listOf(ttlSeconds))
    }

    // 일일 활성 사용자수 기록하기
    fun recordUserActivity(userId: Long) {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        val key = "$DAILY_ACTIVE_USERS_KEY_PREFIX$today"
        redisClient.eval(saddExpireLua, listOf(key), listOf(userId.toString(), ttlSeconds))
    }

    companion object {
        const val LINK_VIEW_COUNT_KEY_PREFIX = "stats:link:view:count:"
        const val DAILY_ACTIVE_USERS_KEY_PREFIX = "stats:user:active:"
    }
}