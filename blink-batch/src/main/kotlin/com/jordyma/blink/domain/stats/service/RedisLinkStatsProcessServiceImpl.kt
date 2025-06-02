package com.jordyma.blink.domain.stats.service

import com.jordyma.blink.redis.client.RedisClient
import com.jordyma.blink.stats.service.LinkStatsProcessService
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class RedisLinkStatsProcessServiceImpl(
    private val redisClient: RedisClient,
) : LinkStatsProcessService {
    override fun saveLinkViewCount(): Long {
        TODO("Not yet implemented")
    }

    override fun getYesterdayLinkViewCount(): Long {
        val yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_DATE)
        return (redisClient.get("${LINK_VIEW_COUNT_KEY_PREFIX}$yesterday")?.toIntOrNull() ?: 0L) as Long
    }

    override fun getYesterdayDailyActiveUsers(): Long {
        val yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_DATE)
        return (redisClient.scard("${DAILY_ACTIVE_USERS_KEY_PREFIX}$yesterday")?.toInt() ?: 0L) as Long
    }

    companion object {
        const val LINK_VIEW_COUNT_KEY_PREFIX = "stats:link:view:count:"
        const val DAILY_ACTIVE_USERS_KEY_PREFIX = "stats:user:active:"
    }
}