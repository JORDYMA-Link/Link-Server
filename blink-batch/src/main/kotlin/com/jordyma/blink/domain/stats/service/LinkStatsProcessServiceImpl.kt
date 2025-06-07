package com.jordyma.blink.domain.stats.service

import com.jordyma.blink.infra.stats.StatsStore
import com.jordyma.blink.stats.service.LinkStatsIncreaseService
import com.jordyma.blink.stats.service.LinkStatsProcessService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class LinkStatsProcessServiceImpl (
    @Qualifier("linkStatsIncreaseServiceImpl")
    private val linkStatsIncreaseService: LinkStatsIncreaseService,
    private val statsStore: StatsStore,
) : LinkStatsProcessService {
    override fun saveLinkViewCount(): Long {
        TODO("Not yet implemented")
    }

    override fun getYesterdayLinkViewCount(): Long {
        val yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_DATE)
        return statsStore.getLinkViewCount(yesterday)
    }

    override fun getYesterdayDailyActiveUsers(): Long {
        val yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_DATE)
        return statsStore.getActiveUserCount(yesterday)
    }
}