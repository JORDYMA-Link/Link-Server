package com.jordyma.blink.domain.notification.dto

data class UserDataNotificationDto (
    val newUserCount: Int = 0,
    val totalUserCount: Int = 0,
    val newUserFeed: Int = 0,
    val existingUserFeed: Int = 0,
    val linkViewCount: Int = 0,  // 일일 링크 조회 횟수
    val dailyActiveUsers: Int = 0 // 일일 활성 사용자수
)