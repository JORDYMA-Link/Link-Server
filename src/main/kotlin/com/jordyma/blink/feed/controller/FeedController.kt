package com.jordyma.blink.feed.controller

import com.jordyma.blink.auth.jwt.user_account.UserAccount
import com.jordyma.blink.feed.dto.AiSummaryResponseDto
import com.jordyma.blink.global.resolver.RequestUserId
import com.jordyma.blink.feed.dto.FeedCalendarResponseDto
import com.jordyma.blink.feed.dto.request.FeedUpdateReqDto
import com.jordyma.blink.feed.dto.response.FeedUpdateResDto
import com.jordyma.blink.feed.dto.response.ProcessingListDto
import com.jordyma.blink.feed.service.FeedService
import com.jordyma.blink.folder.service.FolderService
import com.jordyma.blink.global.gemini.api.GeminiService
import com.jordyma.blink.image.dto.response.ImageCreateResDto
import com.jordyma.blink.image.service.ImageService
import com.jordyma.blink.user.dto.UserInfoDto
import com.jordyma.blink.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/feeds")
class FeedController(
    private val feedService: FeedService,
    private val userService: UserService,
    private val folderService: FolderService,
    private val geminiService: GeminiService,
    private val imageService: ImageService
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
    @GetMapping("/summary")
    fun getAiSummary(
        @AuthenticationPrincipal userAccount: UserAccount,
        @RequestParam("link") link: String,
    ): ResponseEntity<AiSummaryResponseDto> {
        val folderNames: List<String> = folderService.getFolders(userAccount).folderList.map { it.name }
        val content = geminiService.getContents(link = link, folders = folderNames.joinToString(separator = " "), userAccount)
        val brunch = feedService.findBrunch(link)

        val response = feedService.makeFeedAndResponse(content, brunch, userAccount, link)
        return ResponseEntity.ok(response)
    }


    @Operation(summary = "링크 저장(수정) api", description = "요약 결과 저장, 수정")
    @PatchMapping("/{feedId}")
    fun createFeed(
        @AuthenticationPrincipal userAccount: UserAccount,
        @RequestBody requestDto: FeedUpdateReqDto,
        @PathVariable feedId: Long,
    ): ResponseEntity<FeedUpdateResDto> {
        val response = feedService.update(userAccount, requestDto, feedId)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "요약 중인 링크 조회 api")
    @GetMapping("/processing")
    fun getProcessing(
        @AuthenticationPrincipal userAccount: UserAccount,
    ): ResponseEntity<ProcessingListDto> {
        val response = feedService.getProcessing(userAccount)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "썸네일 이미지 업로드 api")
    @PostMapping("/image/{feedId}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createThumbnailImage(
        @AuthenticationPrincipal userAccount: UserAccount,
        @RequestPart(value = "thumbnailImage") thumbnailImage: MultipartFile,
        @PathVariable feedId: Long,
    ): ResponseEntity<ImageCreateResDto> {
        val response = imageService.uploadThumbnailImage(thumbnailImage, feedId)
        return ResponseEntity.ok(response)
    }
}
