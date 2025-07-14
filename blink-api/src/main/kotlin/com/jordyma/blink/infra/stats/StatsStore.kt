package com.jordyma.blink.infra.stats

import com.jordyma.blink.logger
import com.jordyma.blink.stats.UserStatisticRepository
import jakarta.annotation.PostConstruct
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Component
class StatsStore (
    private val userStatisticRepository: UserStatisticRepository,
){
    private val linkViewCounts = ConcurrentHashMap<String, AtomicLong>()
    private val dailyActiveUsers = ConcurrentHashMap<String, ConcurrentHashMap.KeySetView<Long, Boolean>>()

    @PostConstruct
    fun initializeFromDatabase() {
        try {
            val today = LocalDateTime.now()
            val todayStr = LocalDate.now().format(DateTimeFormatter.ISO_DATE)

            // 링크 조회수
            val todayLinkViews = userStatisticRepository.findByTypeAndDate("LINK_VIEW", today)
            if (todayLinkViews != null) {
                linkViewCounts[todayStr] = AtomicLong(todayLinkViews.count)
            }

            // 활성 사용자 수
            val todayActiveUsers = userStatisticRepository.findByTypeAndDate("ACTIVE_USER", today)
            if (todayActiveUsers != null) {
                // 일단은 더미 데이터로 count만 채우기
                val userSet = ConcurrentHashMap.newKeySet<Long>()
                repeat(todayActiveUsers.count.toInt()) { index ->
                    userSet.add(-(index + 1).toLong())
                }
                dailyActiveUsers[todayStr] = userSet
            }

            logger().info("StatsStore initialized from database successfully")
        } catch (e: Exception) {
            logger().error("Failed to initialize StatsStore from database", e.message)
        }
    }


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
        return (dailyActiveUsers[date]?.size ?: 0L).toLong()
    }

    @Scheduled(cron = "0 1 0 * * *")
    @Transactional
    fun saveDailyStatsToDb() {
        val cutoffDate = LocalDate.now().minusDays(3).format(DateTimeFormatter.ISO_DATE)

        linkViewCounts.keys.removeIf { it < cutoffDate }
        dailyActiveUsers.keys.removeIf { it < cutoffDate }
    }
}