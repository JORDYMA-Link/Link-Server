package com.jordyma.blink.infra.stats

import com.jordyma.blink.stats.service.LinkStatsIncreaseService
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class LinkStatsIncreaseServiceImpl(
    private val jdbcTemplate: JdbcTemplate,
    private val statsStore: StatsStore,
) : LinkStatsIncreaseService{

    override fun incrementLinkView() {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        statsStore.countLinkView(today)
    }

    override fun recordUserActivity(userId: Long) {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        statsStore.countActiveUser(userId, today)
    }
}