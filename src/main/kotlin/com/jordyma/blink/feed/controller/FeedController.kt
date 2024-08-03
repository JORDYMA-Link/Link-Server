package com.jordyma.blink.feed.controller

import FeedDetailDto
import com.jordyma.blink.global.resolver.RequestUserId
import com.jordyma.blink.feed.dto.FeedCalendarResponseDto
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

    @Operation(summary = "AI가 요약한 내용 조회 api")
    @GetMapping("/summary/{link}")
    fun getAiSummary(
        @PathVariable link: String,
        @RequestUserId userId: Long
    ): ResponseEntity<AiSummaryResponseDto> {
        val folderNames: List<String> = folderService.getFolderNames(userId)
        val response = geminiService.getContents(link = link, folders = folderNames.joinToString(separator = " "))

        // keywords, folders 추출
        val keywords = response.optJSONArray("keyword")?.let { array ->
            List(array.length()) { index -> array.optString(index) }
        }
        val folders = response.optJSONArray("category")?.let { array ->
            List(array.length()) { index -> array.optString(index) }
        }

        return ResponseEntity.ok(AiSummaryResponseDto.of(
            AiSummaryContent.of(response, keywords as List<String>, folders as List<String>), "url", "folder"))
    }
}
