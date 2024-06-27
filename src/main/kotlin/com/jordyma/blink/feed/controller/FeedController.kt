package com.jordyma.blink.feed.controller

import com.jordyma.blink.global.resolver.RequestUserId
import com.jordyma.blink.feed.dto.FeedCalendarResponseDto
import com.jordyma.blink.feed.service.FeedService
import com.jordyma.blink.user.dto.UserInfoDto
import com.jordyma.blink.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/feeds")
class FeedController(
    private val feedService: FeedService,
    private val userService: UserService
) {
    @GetMapping("")
    fun getFeedsByDate(
        @RequestParam("yearMonth") yearMonth: String,
        @RequestUserId userId: Long
    ): ResponseEntity<Map<String, FeedCalendarResponseDto>> {
        val userDto: UserInfoDto = userService.find(userId)
        val response = feedService.getFeedsByMonth(user = userDto, yrMonth = yearMonth)
        return ResponseEntity.ok(response)
    }
}
