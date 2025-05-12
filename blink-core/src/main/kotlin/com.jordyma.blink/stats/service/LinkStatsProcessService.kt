package com.jordyma.blink.stats.service

interface LinkStatsProcessService {
    // 일일 링크 조회수 저장
    fun saveLinkViewCount(): Int

    // 일일 링크 조회수 조회
    fun getYesterdayLinkViewCount(): Int

    // 일일 활성 사용자수 조회
    fun getYesterdayDailyActiveUsers(): Int
}