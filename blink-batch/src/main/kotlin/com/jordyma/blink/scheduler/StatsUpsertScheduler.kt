package com.jordyma.blink.scheduler

import com.jordyma.blink.infra.stats.StatsStore
import com.jordyma.blink.logger
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class StatsUpsertScheduler(
    private val statsStore: StatsStore,
    private val jdbcTemplate: JdbcTemplate,
) {

    @Scheduled(cron = "0 */30 * * * *")
    fun upsertHourlyStats() {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        val todayMidnight = LocalDate.now().atStartOfDay()

        val linkViewCount = statsStore.getLinkViewCount(today)
        if (linkViewCount > 0) {
            upsertUserStatistic(LINK_VIEW, linkViewCount, todayMidnight)
        }

        val activeUserCount = statsStore.getActiveUserCount(today)
        if (activeUserCount > 0) {
            upsertUserStatistic(ACTIVE_USER, activeUserCount, todayMidnight)
        }
    }

    fun upsertUserStatistic(type: String, count: Long, dateTime: LocalDateTime) {
        try {
            val existingCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM user_statistic WHERE type = ? AND DATE(date) = DATE(?)",
                Int::class.java,
                type, dateTime
            )

            if (existingCount > 0) {
                jdbcTemplate.update(
                    "UPDATE user_statistic SET count = ? WHERE type = ? AND DATE(date) = DATE(?)",
                    count, type, dateTime
                )
            } else {
                jdbcTemplate.update(
                    "INSERT INTO user_statistic (type, count, date) VALUES (?, ?, ?)",
                    type, count, dateTime
                )
            }
        } catch (e: Exception) {
            logger().error("업데이트 실패: ${e.message}", e)
        }
    }

    companion object {
        const val ACTIVE_USER = "ACTIVE_USER"
        const val LINK_VIEW = "LINK_VIEW"
    }
}