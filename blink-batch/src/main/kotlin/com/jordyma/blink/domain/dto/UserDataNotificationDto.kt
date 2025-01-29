package com.jordyma.blink.domain.dto

data class UserDataNotificationDto (
    val newUserCount: Int,
    val totalUserCount: Int,
    val newUserFeed: Int,
    val existingUserFeed: Int,
)