package com.jordyma.blink.feed.controller

import com.jordyma.blink.auth.jwt.user_account.UserAccount
import com.jordyma.blink.feed.dto.AiSummaryResponseDto
import com.jordyma.blink.feed.dto.FeedCalendarResponseDto
import com.jordyma.blink.feed.dto.request.FeedUpdateReqDto
import com.jordyma.blink.feed.dto.request.TempReqDto
import com.jordyma.blink.feed.dto.response.FeedUpdateResDto
import com.jordyma.blink.feed.dto.response.ProcessingListDto
import com.jordyma.blink.feed.dto.FeedDetailDto
import com.jordyma.blink.feed.service.FeedService
import com.jordyma.blink.folder.service.FolderService
import com.jordyma.blink.global.gemini.api.GeminiService
import com.jordyma.blink.image.dto.response.ImageCreateResDto
import com.jordyma.blink.image.service.ImageService
import com.jordyma.blink.logger
import com.jordyma.blink.user.dto.UserInfoDto
import com.jordyma.blink.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import lombok.extern.java.Log

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
    @GetMapping("by-date")
    fun getFeedsByDate(
        @RequestParam("yearMonth") yearMonth: String,
        @AuthenticationPrincipal userAccount: UserAccount,
    ): ResponseEntity<FeedCalendarResponseDto> {
        val userDto: UserInfoDto = userService.find(userAccount.userId)
        val response = feedService.getFeedsByMonth(user = userDto, yrMonth = yearMonth)
        return ResponseEntity.ok(response)
    }

    @Tag(name = "link", description = "링크 API")
    @Operation(summary = "링크 요약 api", description = "요약 결과 확인")
    @PostMapping("/summary")
    fun getAiSummary(
        @AuthenticationPrincipal userAccount: UserAccount,
       // @RequestParam("link") link: String,
        @RequestBody requestDto: TempReqDto,
    ): ResponseEntity<AiSummaryResponseDto> {
        val folderNames: List<String> = folderService.getFolders(userAccount).folderList.map { it.name }
        val content = geminiService.getContents(link = requestDto.link, folders = folderNames.joinToString(separator = " "), userAccount, requestDto.content)
        val brunch = feedService.findBrunch(requestDto.link)

        val response = feedService.makeFeedAndResponse(content, brunch, userAccount, requestDto.link)
        return ResponseEntity.ok(response)
    }

    @Tag(name = "link", description = "링크 API")
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

    @Tag(name = "link", description = "링크 API")
    @Operation(summary = "요약 중인 링크 조회 api")
    @GetMapping("/processing")
    fun getProcessing(
        @AuthenticationPrincipal userAccount: UserAccount,
    ): ResponseEntity<ProcessingListDto> {
        val response = feedService.getProcessing(userAccount)
        return ResponseEntity.ok(response)
    }

    @Tag(name = "link", description = "링크 API")
    @Operation(summary = "요약 불가 링크 삭제 api")
    @DeleteMapping("/processing/{feedId}")
    fun deleteProcessingFeed(
        @AuthenticationPrincipal userAccount: UserAccount,
        @PathVariable feedId: Long,
    ): ResponseEntity<String> {
        feedService.deleteProcessingFeed(userAccount, feedId)
        return ResponseEntity.ok("삭제 완료되었습니다.")
    }

    @Tag(name = "link", description = "링크 API")
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

    @Operation(summary = "피드 상세 조회 api", description = "피드 아이디를 pathVariable로 넣어주면, 해당 피드id의 상세 정보를 반환해줍니다.")
    @GetMapping("/{feedId}")
    fun getFeedDetail(
        @PathVariable("feedId") @Parameter(description = "피드 아이디", required = true) feedId: Long,
        @AuthenticationPrincipal userAccount: UserAccount
    ): ResponseEntity<FeedDetailDto> {
        logger().info("getFeedDetail called : feedId = $feedId")

        val userDto: UserInfoDto = userService.find(userAccount.userId)
        val feedDetailDto = feedService.getFeedDetail(user = userDto, feedId = feedId)
        return ResponseEntity.ok(feedDetailDto)
    }

}
