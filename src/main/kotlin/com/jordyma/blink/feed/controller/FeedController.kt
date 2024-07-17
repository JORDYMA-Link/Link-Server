package com.jordyma.blink.feed.controller

import com.jordyma.blink.global.resolver.RequestUserId
import com.jordyma.blink.feed.dto.FeedCalendarResponseDto
import com.jordyma.blink.feed.dto.FeedDetailDto
import com.jordyma.blink.feed.service.FeedService
import com.jordyma.blink.user.dto.UserInfoDto
import com.jordyma.blink.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/feeds")
class FeedController(
    private val feedService: FeedService,
    private val userService: UserService
) {
    @Operation(summary = "캘린더 피드 검색 api", description = "년도와 월(yyyy-MM)을 param으로 넣어주면, 해당 월의 피드들을 반환해줍니다.")
    @GetMapping("")
    fun getFeedsByDate(
        @RequestParam("yearMonth") yearMonth: String,
        @RequestUserId userId: Long
    ): ResponseEntity<FeedCalendarResponseDto> {
        val userDto: UserInfoDto = userService.find(userId)
        val response = feedService.getFeedsByMonth(user = userDto, yrMonth = yearMonth)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "피드 상세 조회 api", description = "피드 아이디를 pathVariable로 넣어주면, 해당 피드id의 상세 정보를 반환해줍니다.")
    @GetMapping("/{feedId}")
    fun getFeedDetail(
        @PathVariable("feedId") @Parameter(description = "피드 아이디", required = true) feedId: Long,
        @RequestUserId userId: Long
    ): ResponseEntity<FeedDetailDto> {
        val userDto: UserInfoDto = userService.find(userId)
        val feedDetailDto = feedService.getFeedDetail(user = userDto, feedId = feedId)
        return ResponseEntity.ok(feedDetailDto)
    }

}
