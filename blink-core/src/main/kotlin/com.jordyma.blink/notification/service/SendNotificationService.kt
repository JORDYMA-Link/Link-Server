package com.jordyma.blink.notification.service

import com.jordyma.blink.feed.domain.Feed

interface SendNotificationService {
    fun sendSummarizedAlert(userId: Long, feed: Feed)
}