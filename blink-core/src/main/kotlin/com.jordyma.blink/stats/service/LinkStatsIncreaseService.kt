package com.jordyma.blink.stats.service

interface LinkStatsIncreaseService {

    // 링크 조회 api 호출시 증가
    fun incrementLinkView()

    // 일일 활성 사용자수 증가
    fun recordUserActivity(userId: Long)
}