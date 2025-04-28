package com.jordyma.blink.stats.service

import com.jordyma.blink.redis.client.RedisClient
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class LinkStatisticsService(private val redisClient: RedisClient) {

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
        redisClient.incr("$LINK_VIEW_COUNT_KEY_PREFIX$today")

        // 만료 기간 3일
        redisClient.expire("$LINK_VIEW_COUNT_KEY_PREFIX$today", 60 * 60 * 24 * 3)
    }

    // 일일 활성 사용자수 기록하기
    fun recordUserActivity(userId: Long) {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        redisClient.sadd("$DAILY_ACTIVE_USERS_KEY_PREFIX$today", userId.toString())

        // 만료 기간 3일
        redisClient.expire("$DAILY_ACTIVE_USERS_KEY_PREFIX$today", 60 * 60 * 24 * 3)
    }

    companion object {
        const val LINK_VIEW_COUNT_KEY_PREFIX = "stats:link:view:count:"
        const val DAILY_ACTIVE_USERS_KEY_PREFIX = "stats:user:active:"
    }
}