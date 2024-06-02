package com.jordyma.blink.feed.controller

import com.jordyma.blink.common.resolver.RequestUserId
import com.jordyma.blink.common.util.DateTimeUtils
import com.jordyma.blink.feed.dto.FeedDateResponseDto
import com.jordyma.blink.feed.service.FeedService
import com.jordyma.blink.user.entity.User
import com.jordyma.blink.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/feeds")
class FeedController @Autowired constructor(
    private val feedService: FeedService,
    private val userService: UserService
) {
    @GetMapping("")
    fun getFeedsByDate(@RequestParam("date") date: String, @RequestUserId userId: Long): ResponseEntity<FeedDateResponseDto> {
        val dateParse: LocalDate = DateTimeUtils.stringToLocalDate(date)
        val user: User = userService.find(userId)
        val feedDateResponseDto = feedService.getFeedsByDate(date = dateParse, user = user)
        return ResponseEntity.ok(feedDateResponseDto)
    }
}
