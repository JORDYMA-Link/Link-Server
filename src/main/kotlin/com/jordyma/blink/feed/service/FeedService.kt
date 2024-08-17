package com.jordyma.blink.feed.service

import com.jordyma.blink.auth.jwt.user_account.UserAccount
import com.jordyma.blink.feed.dto.*
import com.jordyma.blink.global.error.KEYWORDS_NOT_FOUND
import com.jordyma.blink.global.error.exception.BadRequestException
import com.jordyma.blink.global.util.rangeTo
import com.jordyma.blink.feed.dto.request.FeedCreateReqDto
import com.jordyma.blink.feed.dto.response.FeedCreateResDto
import com.jordyma.blink.feed.entity.Source
import com.jordyma.blink.feed.entity.Feed
import com.jordyma.blink.feed.entity.Status
import com.jordyma.blink.feed.repository.FeedRepository
import com.jordyma.blink.folder.entity.Folder
import com.jordyma.blink.folder.entity.Recommend
import com.jordyma.blink.folder.repository.FolderRepository
import com.jordyma.blink.folder.repository.RecommendRepository
import com.jordyma.blink.folder.service.FolderService
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.global.gemini.response.PromptResponse
import com.jordyma.blink.keyword.entity.Keyword
import com.jordyma.blink.keyword.repository.KeywordRepository
import com.jordyma.blink.user.dto.UserInfoDto
import com.jordyma.blink.user.entity.User
import com.jordyma.blink.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Service
class FeedService(
    private val folderService: FolderService,
    private val feedRepository: FeedRepository,
    private val keywordRepository: KeywordRepository,
    private val userRepository: UserRepository,
    private val folderRepository: FolderRepository,
    private val recommendRepository: RecommendRepository
) {

    @Transactional(readOnly = true)
    fun getFeedsByMonth(user: UserInfoDto, yrMonth: String): FeedCalendarResponseDto {
        val yearMonth = YearMonth.parse(yrMonth, DateTimeFormatter.ofPattern("yyyy-MM"))
        val startOfMonth = yearMonth.atDay(1).atStartOfDay()
        val endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59)

        val feeds = feedRepository.findFeedFolderDtoByUserIdAndBetweenDate(user.id, startOfMonth, endOfMonth)
        val feedsByDate = feeds.groupBy { it.feed.createdAt?.toLocalDate() }

        val response = mutableMapOf<String, FeedCalendarListDto>()

        for (date in startOfMonth.toLocalDate().rangeTo(endOfMonth.toLocalDate())) {
            val feedItems = feedsByDate[date]?.map { feedFolderDto ->
                FeedDto(
                    folderId = feedFolderDto.folderId,
                    folderName = feedFolderDto.folderName,
                    feedId = feedFolderDto.feed.id!!,
                    title = feedFolderDto.feed.title,
                    summary = feedFolderDto.feed.summary,
                    platform = feedFolderDto.feed.source!!,
                    sourceUrl = feedFolderDto.feed.url,
                    isMarked = feedFolderDto.feed.isMarked,
                    keywords = getKeywordsByFeedId(feedFolderDto.feed.id) // 키워드 추출 함수
                )
            } ?: emptyList()

            val isArchived = feedItems.isNotEmpty()

            response[date.toString()] = FeedCalendarListDto(
                isArchived = isArchived,
                list = feedItems
            )
        }

        return FeedCalendarResponseDto(response)
    }

    private fun getKeywordsByFeedId(feedId: Long): List<String> {
        val keywords = keywordRepository.findByFeedId(feedId)
        if (keywords.isEmpty()) {
            throw BadRequestException(KEYWORDS_NOT_FOUND)
        }
        return keywords.map { it.keyword }
    }

    // 피드 생성
    fun create(userAccount: UserAccount, request: FeedCreateReqDto): FeedCreateResDto? {

        // 유저 확인
        val user = findUserOrElseThrow(userAccount.userId)

        // 기존 폴더 확인 or 새 폴더 생성
        val folder = checkFolder(user, request.folderName)

        // 피드 생성
        val brunch = findBrunch(request.url)
        val feed = Feed(
            folder = folder!!,
            url = request.url,
            summary = request.summary,
            title = request.title,
            memo = request.memo,
            source = brunch.source,
        )
        feedRepository.save(feed)

        // 미분류인 경우 - 추천 폴더 생성
        if(folder!!.name == "미분류"){
            createRecommendFolders(feed, request)
        }

        // 키워드 생성
        createKeywords(feed, request)

        if(feed.id == null){
            throw ApplicationException(ErrorCode.NOT_CREATED, "피드 생성 오류")
        }
        return FeedCreateResDto(feed.id)
    }

    // 요약 실패 피드 생성
    fun createFailed(userAccount: UserAccount, link: String) {
        val user = findUserOrElseThrow(userAccount.userId)
        val failedFolder = folderService.getFailed(userAccount)
        val feed = Feed(
            folder = failedFolder,
            url = link,
            summary = "",
            title = "",
            status = Status.FAILED,
        )
        feedRepository.save(feed)
    }

    fun makeFeedAndResponse(content: PromptResponse?, brunch: Source, userAccount: UserAccount, link: String): AiSummaryResponseDto? {
        val feed = makeFeed(userAccount, content, brunch, link)  // 피드 저장
        createRecommendFoldersV2(feed, content)
        return makeAiSummaryResponse(content, brunch, feed.id!!)
    }

    private fun makeFeed(userAccount: UserAccount, content: PromptResponse?, brunch: Source, link: String): Feed {
        val user = findUserOrElseThrow(userAccount.userId)
        val folder = folderService.getUnclassified(userAccount)

        // ai 요약 결과로 피드 생성 (유저 매칭을 위해 폴더는 미분류로 지정)
        val feed = Feed(
            folder = folder!!,
            url = link,
            summary = content?.summary ?: "",
            title = content?.subject ?: "",
            memo = "",
            source = brunch.source,
            status = Status.REQUESTED,
        )
        return feedRepository.save(feed)
    }

    private fun makeAiSummaryResponse(content: PromptResponse?, source: Source, feedId: Long): AiSummaryResponseDto {
        return AiSummaryResponseDto(
            content = AiSummaryContent.from(content),
            sourceUrl = source.image,
            recommendFolder = content?.category?.get(0) ?: "",
            recommendFolders = content?.category ?: emptyList(),
            feedId = feedId,
            // TODO: gemini가 json으로 답하지 않는 경우 처리 필요
        )
    }

    fun createRecommendFolders(feed: Feed, request: FeedCreateReqDto) {
        var cnt = 0
        val recommendFolders: MutableList<Recommend> = mutableListOf()
        for (folderName in request.recommendFolders) {
            val recommend = Recommend(
                feed = feed,
                folderName = folderName,
                priority = cnt
            )
            recommendRepository.save(recommend)
            recommendFolders.add(recommend)
            cnt++
        }
        feed.recommendFolders = recommendFolders
    }

    fun createRecommendFoldersV2(feed: Feed, content: PromptResponse?) {
        var cnt = 0
        val recommendFolders: MutableList<Recommend> = mutableListOf()
        for (folderName in content!!.category) {
            val recommend = Recommend(
                feed = feed,
                folderName = folderName,
                priority = cnt
            )
            recommendRepository.save(recommend)
            recommendFolders.add(recommend)
            cnt++
        }
        feed.recommendFolders = recommendFolders
    }

    fun createKeywords(feed: Feed, request: FeedCreateReqDto) {
        val createdKeywords: MutableList<Keyword> = mutableListOf()
        for (keyword in request.keywords) {
            val createdKeyword = Keyword(
                feed = feed,
                keyword = keyword
            )
            keywordRepository.save(createdKeyword)
            createdKeywords.add(createdKeyword)
        }
        feed.updateKeywords(createdKeywords)
    }

    fun checkFolder(user: User, folderName: String): Folder? {
        var folder = folderRepository.findAllByUser(user).firstOrNull { it.name == folderName }
        if(folder == null){
            folder = Folder(
                name = folderName,
                user = user,
                count = 0,
                isUnclassified = folderName == "미분류"
            )
        }
        return folder
    }

    fun findBrunch(link: String): Source {
        return if(link.contains("blog.naver.com")){
            Source.NAVER_BLOG
        } else if (link.contains("velog.io")){
            Source.VELOG
        } else if (link.contains("brunch.co.kr")){
            Source.BRUNCH
        } else if (link.contains("yozm.wishket")){
            Source.YOZM_IT
        } else if (link.contains("tistory.com")){
            Source.TISTORY
        } else if (link.contains("eopla.net")){
            Source.EO
        } else{
            Source.DEFAULT
        }
    }

    fun findUserOrElseThrow(userId: Long): User {
        return userRepository.findById(userId).orElseThrow {
            ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.")
        }
    }
}
