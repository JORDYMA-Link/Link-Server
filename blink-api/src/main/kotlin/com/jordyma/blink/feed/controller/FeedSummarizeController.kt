package com.jordyma.blink.feed.controller

import com.jordyma.blink.auth.jwt.user_account.UserAccount
import com.jordyma.blink.featureflag.FeatureFlagService
import com.jordyma.blink.feed.domain.model.FeedSummarizeMessage
import com.jordyma.blink.feed.dto.AiSummaryResponseDto
import com.jordyma.blink.feed.dto.FeedIdResponseDto
import com.jordyma.blink.feed.dto.request.FeedUpdateReqDto
import com.jordyma.blink.feed.dto.request.LinkRequestDto
import com.jordyma.blink.feed.dto.response.ChallengeResDto
import com.jordyma.blink.feed.dto.response.FeedUpdateResDto
import com.jordyma.blink.feed.dto.response.ProcessingListDto
import com.jordyma.blink.feed.service.FeedService
import com.jordyma.blink.feed.service.FeedSummarizeService
import com.jordyma.blink.feed.strategy.SummarizationExecutorFactory
import com.jordyma.blink.infra.ratelimit.RateLimit
import com.jordyma.blink.logger
import com.jordyma.blink.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.coroutineScope
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/feeds")
class FeedSummarizeController(
    private val feedService: FeedService,
    private val userService: UserService,
    private val feedSummarizeService: FeedSummarizeService,
    private val featureFlagService: FeatureFlagService,
    private val executorFactory: SummarizationExecutorFactory,
) {
    private val log = org.slf4j.LoggerFactory.getLogger(this::class.java)
    
    @RateLimit
    @Tag(name = "link", description = "링크 API")
    @Operation(summary = "[링크 요약 1] 링크 요약 api", description = "링크 요약 요청 전송, ai 요약 결과 저장")
    @PostMapping("/summary")
    suspend fun getAiSummary(
        @AuthenticationPrincipal userAccount: UserAccount,
        @RequestBody requestDto: LinkRequestDto,
    ): ResponseEntity<FeedIdResponseDto> = coroutineScope {
        val feed = feedService.makeFeedFirst(userAccount, requestDto.link)
        val userInfo = userService.find(userAccount)

        // FeedSummarizeMessage 생성
        val summarizeMessage = FeedSummarizeMessage(
            link = requestDto.link,
            feedId = feed.id,
            userId = userAccount.userId,
            originUrl = feed.originUrl,
            userName = userInfo.name
        )

        // Invalid link 체크
        if (feedSummarizeService.isInvalidLink(requestDto.link)) {
            log.info("FAILED: Invalid link: ${requestDto.link}")
            feedService.createFailed(userAccount, feed.id)
            return@coroutineScope ResponseEntity.ok(FeedIdResponseDto(feedId = feed.id))
        }

        // Feature Flag 기반 전략 선택
        val strategy = featureFlagService.getSummarizationStrategy(userAccount.userId)
        val executor = executorFactory.getExecutor(strategy)
        
        log.info("Processing with strategy: $strategy, feedId=${feed.id}, userId=${userAccount.userId}")
        
        // 비동기 실행 (suspend fun으로 호출, 즉시 반환)
        val feedId = executor.execute(summarizeMessage)
        
        // 응답은 즉시 반환 (202 Accepted)
        ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body(FeedIdResponseDto(feedId = feedId))
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

    @Tag(name = "link", description = "링크 API")
    @Operation(summary = "프로모션 기간 챌린지 api", description = "모달 표시 여부, 1 ~ 5: 각각 n번 저장됨")
    @GetMapping("/challenge")
    fun getChallengeStatus(
        @AuthenticationPrincipal userAccount: UserAccount
    ): ResponseEntity<ChallengeResDto> {
        val response = feedService.getChallengeStatus(userAccount.userId)
        return ResponseEntity.ok(response)
    }

    @Tag(name = "link", description = "링크 API")
    @Operation(summary = "프로모션 기간 챌린지 api", description = "모달 표시 여부, 1 ~ 5: 각각 n번 저장됨")
    @GetMapping("/challenge/test")
    fun getChallengeStatus(
        @AuthenticationPrincipal userAccount: UserAccount,
        @RequestParam("count") count: Int,
    ): ResponseEntity<ChallengeResDto> {
        val response = feedService.getChallengeStatusTest(userAccount.userId, count)
        return ResponseEntity.ok(response)
    }

}