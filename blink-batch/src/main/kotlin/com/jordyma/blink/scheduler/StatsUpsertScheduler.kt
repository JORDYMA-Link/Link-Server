package com.jordyma.blink.scheduler

import com.jordyma.blink.infra.stats.StatsStore
import com.jordyma.blink.logger
import com.jordyma.blink.stats.UserStatistic
import com.jordyma.blink.stats.UserStatisticRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Component
class StatsUpsertScheduler(
    private val statsStore: StatsStore,
    private val userStatisticRepository: UserStatisticRepository,
) {

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    fun upsertHourlyStats() {
        try {
            val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
            val now = LocalDateTime.now()

            // 링크 조회수 upsert
            val linkViewCount = statsStore.getLinkViewCount(today)
            if (linkViewCount > 0) {
                upsertUserStatistic(linkView, linkViewCount, now)
            }

            // 활성 사용자 수 upsert
            val activeUserCount = statsStore.getActiveUserCount(today)
            if (activeUserCount > 0) {
                upsertUserStatistic(activeUser, activeUserCount, now)
            }
        } catch (e: Exception) {
            logger().error("Failed to upsert stats", e.message)
        }
    }

    // @Transactional
    fun upsertUserStatistic(type: String, count: Long, dateTime: LocalDateTime) {
        val existingStat = userStatisticRepository.findByTypeAndDate(type, dateTime)

        if (existingStat != null) {
            existingStat.updateCount(count)
            userStatisticRepository.save(existingStat)
        } else {
            val newStat = UserStatistic(
                type = type,
                count = count,
                date = LocalDateTime.now()
            )
            userStatisticRepository.save(newStat)

        }
    }

    companion object{
        const val activeUser= "ACTIVE_USER"
        const val linkView = "LINK_VIEW"
    }
}