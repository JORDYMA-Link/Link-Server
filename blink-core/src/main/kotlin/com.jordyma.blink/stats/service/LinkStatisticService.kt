package com.jordyma.blink.stats.service

interface LinkStatisticService {
    // 일일 링크 조회수 조회
    fun getYesterdayLinkViewCount(): Int

    // 일일 활성 사용자수 조회
    fun getYesterdayDailyActiveUsers(): Int

    // 링크 조회 api 호출시 증가
    fun incrementLinkView()

    // 일일 활성 사용자수 증가
    fun recordUserActivity(userId: Long)
}