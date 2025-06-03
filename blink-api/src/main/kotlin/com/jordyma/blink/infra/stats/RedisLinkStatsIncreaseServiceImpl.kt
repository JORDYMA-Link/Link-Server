package com.jordyma.blink.infra.stats

import com.jordyma.blink.redis.client.RedisClient
import com.jordyma.blink.stats.service.LinkStatsIncreaseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class RedisLinkStatsIncreaseServiceImpl(
    @Autowired(required = false)
    private val redisClient: RedisClient?,
) : LinkStatsIncreaseService {
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

    // 링크 조회 api 호출시 증가
    override fun incrementLinkView() {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        val key = "${LINK_VIEW_COUNT_KEY_PREFIX}$today"
        redisClient?.eval(incrExpireLua, listOf(key), listOf(ttlSeconds))
    }

    // 일일 활성 사용자수 증가
    override fun recordUserActivity(userId: Long) {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        val key = "${DAILY_ACTIVE_USERS_KEY_PREFIX}$today"
        redisClient?.eval(saddExpireLua, listOf(key), listOf(userId.toString(), ttlSeconds))
    }

    companion object {
        const val LINK_VIEW_COUNT_KEY_PREFIX = "stats:link:view:count:"
        const val DAILY_ACTIVE_USERS_KEY_PREFIX = "stats:user:active:"
    }
}