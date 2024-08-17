package com.jordyma.blink.feed.controller

import com.jordyma.blink.auth.jwt.user_account.UserAccount
import com.jordyma.blink.feed.dto.*
import com.jordyma.blink.feed.service.FeedService
import com.jordyma.blink.user.dto.UserInfoDto
import com.jordyma.blink.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import lombok.extern.java.Log
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/feeds")
class FeedController(
    private val feedService: FeedService,
    private val userService: UserService
) {
    val log: org.slf4j.Logger = LoggerFactory.getLogger(Log::class.java)

    @Operation(summary = "캘린더 피드 검색 api", description = "년도와 월(yyyy-MM)을 param으로 넣어주면, 해당 월의 피드들을 날짜를 Key로 반환해줍니다.")
    @GetMapping("/by-date")
    fun getFeedsByDate(
        @Schema(description = "년도와 월(yyyy-MM)", example = "2024-08")
        @RequestParam("yearMonth") yearMonth: String,
        @AuthenticationPrincipal userAccount: UserAccount
    ): ResponseEntity<FeedCalendarResponseDto> {
        log.info("getFeedsByDate called : yearMonth = $yearMonth")
        val userDto: UserInfoDto = userService.find(userAccount.userId)
        try {
            val response = feedService.getFeedsByMonth(user = userDto, yrMonth = yearMonth)
            return ResponseEntity.ok(response)
        } catch (e: Exception){
            log.error("Error in getFeedsByMonth: ", e)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        }

    }

    @Operation(summary = "피드 상세 조회 api", description = "피드 아이디를 pathVariable로 넣어주면, 해당 피드id의 상세 정보를 반환해줍니다.")
    @GetMapping("/{feedId}")
    fun getFeedDetail(
        @PathVariable("feedId") @Parameter(description = "피드 아이디", required = true) feedId: Long,
        @AuthenticationPrincipal userAccount: UserAccount
    ): ResponseEntity<FeedDetailDto> {
        log.info("getFeedDetail called : feedId = $feedId")

        val userDto: UserInfoDto = userService.find(userAccount.userId)
        val feedDetailDto = feedService.getFeedDetail(user = userDto, feedId = feedId)
        return ResponseEntity.ok(feedDetailDto)
    }


    @Operation(summary = "피드 삭제 api", description = "피드 아이디를 pathVariable로 넣어주면, 해당 피드id를 삭제합니다.")
    @DeleteMapping("/{feedId}")
    fun deleteFeed(
        @PathVariable("feedId") @Parameter(description = "피드 아이디", required = true) feedId: Long,
        @AuthenticationPrincipal userAccount: UserAccount
    ): ResponseEntity<Unit> {
        val userDto: UserInfoDto = userService.find(userAccount.userId)
        feedService.deleteFeed(user = userDto, feedId = feedId)
        return ResponseEntity.noContent().build()
    }


    @Operation(summary = "피드 중요(북마크) 여부 변경 api", description = "setMarked=true/false 에 따라 피드의 중요(북마크) 여부가 변경됩니다.")
    @PatchMapping("/{feedId}")
    fun changeFeedIsMarked(
        @PathVariable feedId: Long,
        @RequestParam setMarked: Boolean,
        @AuthenticationPrincipal userAccount: UserAccount
    ): ResponseEntity<FeedIsMarkedResponseDto> {
        val userDto: UserInfoDto = userService.find(userAccount.userId)
        val responseDto = feedService.changeIsMarked(user = userDto, feedId = feedId, setMarked = setMarked)
        return ResponseEntity.ok(responseDto)
    }

    @Operation(summary = "중요/미분류 피드 리스트 조회 api", description = "type은 BOOKMARKED / UNCLASSIFIED (String)으로 구분됩니다.")
    @GetMapping("/by-type")
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
    @GetMapping("/search")
    fun searchFeeds(
        @RequestParam("query") query: String,
        @RequestParam("page") page: Int,
        @RequestParam("size") size: Int,
        @AuthenticationPrincipal userAccount: UserAccount
    ): ResponseEntity<FeedSearchResponseDto> {
        log.info("searchFeeds called : query = $query")
        val userDto: UserInfoDto = userService.find(userAccount.userId)
        val responseDto = feedService.searchFeeds(user = userDto, query = query, page = page, size = size)
        return ResponseEntity.ok(FeedSearchResponseDto(query = query, result = responseDto))
    }

}
