package com.jordyma.blink.infra.stats

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Component
class StatsStore {
    private val linkViewCounts = ConcurrentHashMap<String, AtomicLong>()
    private val dailyActiveUsers = ConcurrentHashMap<String, ConcurrentHashMap.KeySetView<Long, Boolean>>()

    // 링크 조회수 count
    fun countLinkView(date: String): Long {
        return linkViewCounts.computeIfAbsent(date) { AtomicLong(0) }.incrementAndGet()
    }

    // 활성 사용자 count
    fun countActiveUser(userId: Long, date: String): Boolean {
        return dailyActiveUsers.computeIfAbsent(date) {
            ConcurrentHashMap.newKeySet<Long>()
        }.add(userId)
    }

    // 링크 조회수 조회
    fun getLinkViewCount(date: String): Long {
        return linkViewCounts[date]?.get() ?: 0L
    }

    // 활성 사용자 수 조회
    fun getActiveUserCount(date: String): Long {
        return (dailyActiveUsers[date]?.size ?: 0L) as Long
    }

    @Scheduled(cron = "0 1 0 * * *")
    @Transactional
    fun saveDailyStatsToDb() {
        val cutoffDate = LocalDate.now().minusDays(3).format(DateTimeFormatter.ISO_DATE)

        linkViewCounts.keys.removeIf { it < cutoffDate }
        dailyActiveUsers.keys.removeIf { it < cutoffDate }
    }
}