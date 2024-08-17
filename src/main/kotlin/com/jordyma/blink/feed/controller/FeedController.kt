package com.jordyma.blink.feed.controller

import com.jordyma.blink.auth.jwt.user_account.UserAccount
import com.jordyma.blink.feed.dto.*
import com.jordyma.blink.global.resolver.RequestUserId
import com.jordyma.blink.feed.service.FeedService
import com.jordyma.blink.user.dto.UserInfoDto
import com.jordyma.blink.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
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
    @GetMapping("by-date")
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


    @Operation(summary = "피드 삭제 api", description = "피드 아이디를 pathVariable로 넣어주면, 해당 피드id를 삭제합니다.")
    @DeleteMapping("/{feedId}")
    fun deleteFeed(
        @PathVariable("feedId") @Parameter(description = "피드 아이디", required = true) feedId: Long,
        @RequestUserId userId: Long
    ): ResponseEntity<Unit> {
        val userDto: UserInfoDto = userService.find(userId)
        feedService.deleteFeed(user = userDto, feedId = feedId)
        return ResponseEntity.noContent().build()
    }


    @Operation(summary = "피드 중요(북마크) 여부 변경 api", description = "setMarked=true/false 에 따라 피드의 중요(북마크) 여부가 변경됩니다.")
    @PatchMapping("/{feedId}")
    fun changeFeedIsMarked(@PathVariable feedId: Long, @RequestUserId userId: Long, @RequestParam setMarked: Boolean,
    ): ResponseEntity<FeedIsMarkedResponseDto> {
        val userDto: UserInfoDto = userService.find(userId)
        val responseDto= feedService.changeIsMarked(user = userDto, feedId = feedId, setMarked = setMarked)
        return ResponseEntity.ok(responseDto)
    }

    @Operation(summary = "중요/미분류 피드 리스트 조회 api", description = "type은 BOOKMARKED / UNCLASSIFIED (String)으로 구분됩니다.")
    @GetMapping("by-type")
    fun getFeedsByType(
        @RequestParam("type") type: String,
        @RequestParam("page") page: Int,
        @RequestParam("size") size: Int,
        @AuthenticationPrincipal userAccount: UserAccount
    ): ResponseEntity<List<FeedTypeResponseDto>> {
        val userDto: UserInfoDto = userService.find(userAccount.userId)
        val feedType = FeedType.valueOf(type.uppercase())
        val response = feedService.getFeedsByType(user = userDto, type = feedType, page = page, size = size)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "피드 일반, 키워드 검색 api", description = "검색어를 param으로 넣어주면, 해당 검색어를 포함하는 피드 리스트를 반환해줍니다.")
    @GetMapping("search")
    fun searchFeeds(
        @RequestParam("query") query: String,
        @RequestParam("page") page: Int,
        @RequestParam("size") size: Int,
        @AuthenticationPrincipal userAccount: UserAccount
    ): ResponseEntity<List<FeedResultDto>> {
        val userDto: UserInfoDto = userService.find(userAccount.userId)
        val responseDto = feedService.searchFeeds(user = userDto, query = query, page = page, size = size)
        return ResponseEntity.ok(responseDto)
    }
}
