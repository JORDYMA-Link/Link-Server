package com.jordyma.blink.feed.controller

import com.jordyma.blink.auth.jwt.user_account.UserAccount
import com.jordyma.blink.feed.dto.AiSummaryResponseDto
import com.jordyma.blink.feed.dto.FeedIdResponseDto
import com.jordyma.blink.feed.dto.request.FeedUpdateReqDto
import com.jordyma.blink.feed.dto.request.LinkRequestDto
import com.jordyma.blink.feed.dto.response.FeedUpdateResDto
import com.jordyma.blink.feed.dto.response.ProcessingListDto
import com.jordyma.blink.feed.service.FeedService
import com.jordyma.blink.feed.service.FeedSummarizeService
import com.jordyma.blink.feed_summarize_requester.sender.dto.FeedSummarizeMessage
import com.jordyma.blink.logger
import com.jordyma.blink.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/feeds")
class FeedSummarizeController(
    private val feedService: FeedService,
    private val userService: UserService,
    private val feedSummarizeService: FeedSummarizeService,

) {
    @Tag(name = "link", description = "링크 API")
    @Operation(summary = "[링크 요약 1] 링크 요약 api", description = "링크 요약 요청 전송, ai 요약 결과 저장")
    @PostMapping("/summary")
    fun getAiSummary(
        @AuthenticationPrincipal userAccount: UserAccount,
        @RequestBody requestDto: LinkRequestDto,
    ): ResponseEntity<FeedIdResponseDto> {
        val feed = feedService.makeFeedFirst(userAccount, requestDto.link)
        val userName = userService.getProfile(userAccount).nickName
        val summarizeMessage = FeedSummarizeMessage(requestDto.link, feed.id, userAccount.userId, feed.originUrl, userName)

        if (feedSummarizeService.isInvalidLink(requestDto.link)){
            logger().info(">>>>> EXCEPTION : Try to summarize invalid link: ${requestDto.link}")
            feedService.createFailed(userAccount, feed.id)
            return ResponseEntity.ok(FeedIdResponseDto(feedId = feed.id))
        }


        // worker 요청 전송
        // feedSummarizeMessageSender.send(summarizeMessage)

        // worker, sqs 의존성 제거
        feedSummarizeService.summarizeFeed(summarizeMessage)

        val feedIdResponseDto = FeedIdResponseDto(
            feedId = feed.id
        )
        return ResponseEntity.ok(feedIdResponseDto)
    }

    @Tag(name = "link", description = "링크 API")
    @Operation(summary = "[링크 요약 2] 요약 중인 링크 조회 api", description = "요약 완료된 링크 확인 가능 (w. feedId)")
    @GetMapping("/processing")
    fun getProcessing(
        @AuthenticationPrincipal userAccount: UserAccount,
    ): ResponseEntity<ProcessingListDto> {
        val response = feedService.getProcessing(userAccount.userId)
        return ResponseEntity.ok(response)
    }


    @Tag(name = "link", description = "링크 API")
    @Operation(summary = "[링크 요약 3] 링크 요약 결과 조회 api", description = "ai 요약 결과 확인 (저장버튼 누르기 전)")
    @GetMapping("/summary/{feedId}")
    fun getSummaryRes(
        @AuthenticationPrincipal userAccount: UserAccount,
        @PathVariable feedId: Long,
    ): ResponseEntity<AiSummaryResponseDto> {
        val response = feedService.getSummaryRes(userAccount.userId, feedId)
        return ResponseEntity.ok(response)
    }

    @Tag(name = "link", description = "링크 API")
    @Operation(summary = "[링크 요약 4] 링크 저장(수정) api", description = "플로우 3으로 내용 확인 후 저장")
    @PatchMapping("/{feedId}")
    fun createFeed(
        @AuthenticationPrincipal userAccount: UserAccount,
        @RequestBody requestDto: FeedUpdateReqDto,
        @PathVariable feedId: Long,
    ): ResponseEntity<FeedUpdateResDto> {
        val response = feedService.update(userAccount, requestDto, feedId)
        return ResponseEntity.ok(response)
    }
}