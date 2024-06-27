package com.jordyma.blink.feed.controller

import com.jordyma.blink.global.resolver.RequestUserId
import com.jordyma.blink.feed.dto.FeedCalendarResponseDto
import com.jordyma.blink.feed.service.FeedService
import com.jordyma.blink.user.entity.User
import com.jordyma.blink.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/feeds")
class FeedController @Autowired constructor(
    private val feedService: FeedService,
    private val userService: UserService
) {
    @GetMapping("")
    fun getFeedsByDate(
        @RequestParam("yearMonth") yearMonth: String,
        @RequestUserId userId: Long
    ): ResponseEntity<Map<String, FeedCalendarResponseDto>> {
        val user: User = userService.find(userId)
        val response = feedService.getFeedsByMonth(user = user, yrMonth = yearMonth)
        return ResponseEntity.ok(response)
    }
}
