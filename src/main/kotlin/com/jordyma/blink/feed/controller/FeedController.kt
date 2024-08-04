package com.jordyma.blink.feed.controller

import com.jordyma.blink.auth.jwt.user_account.UserAccount
import com.jordyma.blink.feed.dto.AiSummaryContent
import com.jordyma.blink.feed.dto.AiSummaryResponseDto
import com.jordyma.blink.global.resolver.RequestUserId
import com.jordyma.blink.feed.dto.FeedCalendarResponseDto
import com.jordyma.blink.feed.dto.request.FeedCreateReqDto
import com.jordyma.blink.feed.dto.response.FeedCreateResDto
import com.jordyma.blink.feed.entity.Brunch
import com.jordyma.blink.feed.service.FeedService
import com.jordyma.blink.folder.dto.request.CreateFolderRequestDto
import com.jordyma.blink.folder.service.FolderService
import com.jordyma.blink.global.gemini.api.GeminiService
import com.jordyma.blink.user.dto.UserInfoDto
import com.jordyma.blink.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/feeds")
class FeedController(
    private val feedService: FeedService,
    private val userService: UserService,
    private val folderService: FolderService,
    private val geminiService: GeminiService,
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

    @Operation(summary = "링크 요약 api", description = "요약 결과 확인")
    @GetMapping("/summary/{link}")
    fun getAiSummary(
        @AuthenticationPrincipal userAccount: UserAccount,
        @PathVariable link: String,
    ): ResponseEntity<AiSummaryResponseDto> {
        val folderNames: List<String> = folderService.getFolders(userAccount).folderList.map { it.name }
        val response = geminiService.getContents(link = link, folders = folderNames.joinToString(separator = " "))
        val brunch = feedService.findBrunch(link)
        return ResponseEntity.ok(AiSummaryResponseDto.of(
            AiSummaryContent.from(response), brunch.image, "folder"))
    }

    @Operation(summary = "링크 저장 api", description = "요약 결과 저장")
    @PostMapping("")
    fun createFeed(
        @AuthenticationPrincipal userAccount: UserAccount,
        @RequestBody requestDto: FeedCreateReqDto,
    ): ResponseEntity<FeedCreateResDto> {
        val response = feedService.create(userAccount = userAccount, request = requestDto)
        return ResponseEntity.ok(response)
    }
}
