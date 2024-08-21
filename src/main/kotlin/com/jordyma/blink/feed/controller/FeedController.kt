package com.jordyma.blink.feed.controller

import com.jordyma.blink.auth.jwt.user_account.UserAccount
import com.jordyma.blink.feed.dto.*
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
import com.jordyma.blink.image.service.ImageService
import com.jordyma.blink.logger
import com.jordyma.blink.user.dto.UserInfoDto
import com.jordyma.blink.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus
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

@RestController
@RequestMapping("/api/feeds")
class FeedController(
    private val feedService: FeedService,
    private val userService: UserService,
    private val folderService: FolderService,
    private val geminiService: GeminiService,
    private val imageService: ImageService
) {

    @Operation(summary = "캘린더 피드 검색 api", description = "년도와 월(yyyy-MM)을 param으로 넣어주면, 해당 월의 피드들을 날짜를 Key로 반환해줍니다.")
    @GetMapping("/by-date")
    fun getFeedsByDate(
        @Schema(description = "년도와 월(yyyy-MM)", example = "2024-08")
        @RequestParam("yearMonth") yearMonth: String,
        @AuthenticationPrincipal userAccount: UserAccount
    ): ResponseEntity<FeedCalendarResponseDto> {
        logger().info("getFeedsByDate called : yearMonth = $yearMonth")
        val userDto: UserInfoDto = userService.find(userAccount.userId)
        try {
            val response = feedService.getFeedsByMonth(user = userDto, yrMonth = yearMonth)
            return ResponseEntity.ok(response)
        } catch (e: Exception){
            logger().error("Error in getFeedsByMonth: ", e)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        }
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
    ): ResponseEntity<String> {
        val response = imageService.uploadThumbnailImage(thumbnailImage, feedId)
        return ResponseEntity.ok("이미지 업로드 완료 $response")
    }

    @Operation(summary = "피드 상세 조회 api", description = "피드 아이디를 pathVariable로 넣어주면, 해당 피드id의 상세 정보를 반환해줍니다.")
    @GetMapping("/detail/{feedId}")
    fun getFeedDetail(
        @PathVariable("feedId") @Parameter(description = "피드 아이디", required = true) feedId: Long,
        @AuthenticationPrincipal userAccount: UserAccount
    ): ResponseEntity<FeedDetailDto> {
        logger().info("getFeedDetail called : feedId = $feedId")

        val feedDetailDto = feedService.getFeedDetail(userAccount = userAccount, feedId = feedId)
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
    @PatchMapping("/bookmark/{feedId}")
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
    ): ResponseEntity<FeedTypeResponseDto> {
        val userDto: UserInfoDto = userService.find(userAccount.userId)
        val feedType = FeedType.valueOf(type.uppercase())
        val response = feedService.getFeedsByType(user = userDto, type = feedType, page = page, size = size)
        return ResponseEntity.ok(FeedTypeResponseDto(feedList = response))
    }


    @Operation(summary = "피드 일반, 키워드 검색 api", description = "검색어를 param으로 넣어주면, 해당 검색어를 포함하는 피드 리스트를 반환해줍니다.")
    @GetMapping("/search")
    fun searchFeeds(
        @RequestParam("query") query: String,
        @RequestParam("page") page: Int,
        @RequestParam("size") size: Int,
        @AuthenticationPrincipal userAccount: UserAccount
    ): ResponseEntity<FeedSearchResponseDto> {
        logger().info("searchFeeds called : query = $query")
        val userDto: UserInfoDto = userService.find(userAccount.userId)
        val responseDto = feedService.searchFeeds(user = userDto, query = query, page = page, size = size)
        return ResponseEntity.ok(FeedSearchResponseDto(query = query, result = responseDto))
    }

}
