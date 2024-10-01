package com.jordyma.blink.feed.service

import com.jordyma.blink.feed.dto.*
import com.jordyma.blink.feed.entity.Feed
import com.jordyma.blink.auth.jwt.user_account.UserAccount
import com.jordyma.blink.global.util.rangeTo
import com.jordyma.blink.feed.dto.request.FeedUpdateReqDto
import com.jordyma.blink.feed.entity.Source
import com.jordyma.blink.feed.entity.Status
import com.jordyma.blink.folder.entity.Folder
import com.jordyma.blink.folder.entity.Recommend
import com.jordyma.blink.folder.repository.FolderRepository
import com.jordyma.blink.folder.repository.RecommendRepository
import com.jordyma.blink.folder.service.FolderService
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.global.gemini.response.PromptResponse
import com.jordyma.blink.keyword.entity.Keyword
import com.jordyma.blink.feed.dto.FeedCalendarListDto
import com.jordyma.blink.feed.vo.ScoredFeedVo
import com.jordyma.blink.global.error.ID_NOT_FOUND
import com.jordyma.blink.global.error.exception.IdRequiredException
import com.jordyma.blink.feed.dto.FeedCalendarResponseDto
import com.jordyma.blink.feed.dto.response.FeedDetailResponseDto
import com.jordyma.blink.feed.dto.response.*
import com.jordyma.blink.feed.repository.FeedRepository
import com.jordyma.blink.global.util.DateTimeUtils.localDateTimeToString
import com.jordyma.blink.keyword.repository.KeywordRepository
import com.jordyma.blink.keyword.service.KeywordService
import com.jordyma.blink.logger
import org.springframework.data.domain.PageRequest
import com.jordyma.blink.user.entity.User
import com.jordyma.blink.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.min

@Service
class FeedService(
    private val folderService: FolderService,
    private val keywordService: KeywordService,
    private val feedRepository: FeedRepository,
    private val keywordRepository: KeywordRepository,
    private val userRepository: UserRepository,
    private val folderRepository: FolderRepository,
    private val recommendRepository: RecommendRepository
) {

    @Transactional(readOnly = true)
    fun getFeedsByMonth(userAccount: UserAccount, yrMonth: String): FeedCalendarResponseDto {
        val yearMonth = YearMonth.parse(yrMonth, DateTimeFormatter.ofPattern("yyyy-MM"))
        val startOfMonth = yearMonth.atDay(1).atStartOfDay()
        val endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59)

        val user = userRepository.findById(userAccount.userId).orElseThrow {
            ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.")
        }
        val feeds = feedRepository.findFeedFolderDtoByUserIdAndBetweenDate(user, startOfMonth, endOfMonth)
        val feedsByDate = feeds.groupBy { it.feed.createdAt?.toLocalDate() }

        val response = mutableMapOf<String, FeedCalendarListDto>() //

        for (date in startOfMonth.toLocalDate().rangeTo(endOfMonth.toLocalDate())) {
            val feedItems = feedsByDate[date]?.map { feedFolderDto ->
                FeedItemDto(
                    folderId = feedFolderDto.folderId,
                    folderName = feedFolderDto.folderName,
                    feedId = feedFolderDto.feed.id ?: throw IdRequiredException(ID_NOT_FOUND),
                    title = feedFolderDto.feed.title,
                    summary = feedFolderDto.feed.summary,
                    platform = feedFolderDto.feed.platform ?: "",
                    platformImage = findBrunch(feedFolderDto.feed.platform ?: "").image,
                    isMarked = feedFolderDto.feed.isMarked,
                    keywords = feedFolderDto.feed.keywords.map { it.content },
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

    @Throws(ApplicationException::class)
    @Transactional(readOnly = true)
    fun getFeedDetail(userAccount: UserAccount, feedId: Long): FeedDetailResponseDto {
        val user = userRepository.findById(userAccount.userId).orElseThrow {
            ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.")
        }
        val feed = feedRepository.findById(feedId).orElseThrow(){
            ApplicationException(ErrorCode.FEED_NOT_FOUND, "피드를 찾을 수 없습니다.")
        }
        val feedDetail = feedRepository.findFeedDetail(user, feedId)
            ?: throw ApplicationException(ErrorCode.NOT_FOUND, "피드가 존재하지 않습니다 : $feedId", Throwable())
        return FeedDetailResponseDto(
            feedId = feedDetail.feedId,
            thumbnailImage = feedDetail.thumbnailImageUrl,
            platformImage = findBrunch(feedDetail.platform).image,
            title = feedDetail.title,
            date = localDateTimeToString(feedDetail.date),
            summary = feedDetail.summary,
            keywords = getKeywordsByFeedId(feedId), // 키워드 추출 함수
            folderName = feedDetail.folderName,
            memo = feedDetail.memo ?: "",
            isMarked = feedDetail.isMarked,
            originUrl = feedDetail.originUrl,
            // new
            folderId = feed.folder?.id!!,
            isUnclassified = feed.folder!!.isUnclassified,
            recommendedFolder = if (feed.folder!!.isUnclassified) getRecommendFoldersByFeedId(feed.id!!) else null,
            platform = feedDetail.platform
        )
    }


    @Transactional
    fun deleteFeed(userAccount: UserAccount, feedId: Long) {
        val user = userRepository.findById(userAccount.userId).orElseThrow {
            ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.")
        }
        val feed = feedRepository.findById(feedId)
            .orElseThrow { ApplicationException(ErrorCode.NOT_FOUND, "일치하는 feedId가 없습니다 : $feedId", Throwable()) }
        if (feed.deletedAt != null) {
            throw ApplicationException(ErrorCode.NOT_FOUND, "이미 삭제된 피드입니다 : $feedId", Throwable())
        }
        if (feed.folder!!.user.id != user.id) {
            throw ApplicationException(ErrorCode.FORBIDDEN, "해당 피드를 삭제할 권한이 없습니다", Throwable())
        }
        feed.updateDeletedAt(LocalDateTime.now())
        feedRepository.save(feed)

        feed.folder!!.decreaseCount()
        folderRepository.save(feed.folder!!)
    }

    @Transactional
    fun updateIsMarked(userAccount: UserAccount, feedId: Long, setMarked: Boolean): FeedIsMarkedResponseDto {
        val userId = userAccount.userId
        val user = userRepository.findById(userId).orElseThrow {
            ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.")
        }
        val feed = feedRepository.findById(feedId)
            .orElseThrow { ApplicationException(ErrorCode.NOT_FOUND, "일치하는 feedId가 없습니다 : $feedId", Throwable()) }
        if (feed.folder!!.user.id != user.id) {
            throw ApplicationException(ErrorCode.FORBIDDEN, "해당 피드를 수정할 권한이 없습니다", Throwable())
        }
        feed.updateIsMarked(setMarked)
        feed.updateUpdatedAt(LocalDateTime.now())
        feedRepository.save(feed)

        val newFeed = getFeed(feedId)
        return FeedIsMarkedResponseDto(
            id = newFeed.id ?: throw IdRequiredException(ID_NOT_FOUND),
            isMarked = newFeed.isMarked,
            modifiedDate = if (newFeed.updatedAt != null) localDateTimeToString(newFeed.updatedAt!!, "yyyy-MM-dd HH:mm:ss") else "9999-12-31 23:59:59"
        )
    }

    @Transactional
    fun updateMemo(userAccount: UserAccount, feedId: Long, memo: String): FeedDetailResponseDto {
        val user = userRepository.findById(userAccount.userId).orElseThrow {
            ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.")
        }
        val feed = feedRepository.findById(feedId)
            .orElseThrow { ApplicationException(ErrorCode.NOT_FOUND, "일치하는 feedId가 없습니다 : $feedId", Throwable()) }
        if (feed.folder!!.user.id != user.id) {
            throw ApplicationException(ErrorCode.FORBIDDEN, "해당 피드를 수정할 권한이 없습니다", Throwable())
        }
        feed.updateMemo(memo)
        feed.updateUpdatedAt(LocalDateTime.now())
        feedRepository.save(feed)

        return FeedDetailResponseDto(
            feedId = feed.id!!,
            thumbnailImage = feed.thumbnailImageUrl,
            platformImage = findBrunch(feed.platform ?: "").image,
            title = feed.title,
            date = localDateTimeToString(feed.updatedAt ?: LocalDateTime.now()),
            summary = feed.summary,
            keywords = getKeywordsByFeedId(feedId), // 키워드 추출 함수
            folderName = feed.folder!!.name,
            memo = feed.memo ?: "",
            isMarked = feed.isMarked,
            originUrl = feed.originUrl,
            folderId = feed.folder!!.id!!,
            // new
            isUnclassified = feed.folder!!.isUnclassified,
            recommendedFolder = if (feed.folder!!.isUnclassified) getRecommendFoldersByFeedId(feed.id) else null,
            platform = feed.platform.toString()
        )
    }

    @Transactional(readOnly = true)
    fun getFeed(feedId: Long): Feed
            = feedRepository.findById(feedId)
        .orElseThrow { ApplicationException(ErrorCode.NOT_FOUND, "일치하는 feedId가 없습니다 : $feedId", Throwable()) }

    @Transactional(readOnly = true)
    fun getFeedsByType(userAccount: UserAccount, type: FeedType, page: Int, size: Int): List<FeedTypeDto> {
        val user = userRepository.findById(userAccount.userId).orElseThrow {
            ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.")
        }
        val pageable = PageRequest.of(page, size)
        val feedList =  when (type) {
            FeedType.BOOKMARKED -> feedRepository.findBookmarkedFeeds(user.id!!, pageable).content
            FeedType.UNCLASSIFIED -> feedRepository.findUnclassifiedFeeds(user.id!!, pageable).content
        }
        if (feedList.isNotEmpty()) logger().info("feedList = ${feedList[0]}")
        return feedList.map { feed ->
            val folder = feed.folder ?: throw ApplicationException(ErrorCode.NOT_FOUND, "Folder가 null입니다. Feed ID=${feed.id}")
            FeedTypeDto(
                feedId = feed.id!!,
                title = feed.title,
                summary = feed.summary,
                platform = feed.platform ?: "",
                platformImage = findBrunch(feed.platform ?: "").image,
                isMarked = feed.isMarked,
                isUnclassified = folder.isUnclassified,
                keywords = feed.keywords.map { it.content },
                recommendedFolder = getRecommendFoldersByFeedId(feed.id),
                folderId = folder.id ?: throw ApplicationException(ErrorCode.NOT_FOUND, "Folder ID가 null입니다. Feed ID=${feed.id}"),
                folderName = folder.name
            )
        }
    }

    @Transactional(readOnly = true)
    fun searchFeeds(userAccount: UserAccount, query: String, page: Int, size: Int): List<FeedResultDto> {
        val fetchSize = size * 5 // 5배로 fetch
        val pageable = PageRequest.of(page / 5, fetchSize) // 실제 페이징 계산 (page/5)
        val feedList = feedRepository.findFeedByQuery(userAccount.userId, query, pageable).content

        // DB에서 가져온 데이터를 가중치에 따라 정렬
        val sortedFeeds = searchAndSortFeeds(query, feedList)

        // 클라이언트에서 요청한 데이터만큼만 반환
        val start = (page % 5) * size  // 클라이언트가 요청한 페이지의 시작 인덱스
        val end = min(start + size, sortedFeeds.size) // 끝 인덱스는 정렬된 데이터 크기 내로 제한
        if (start > end) return emptyList()
        return sortedFeeds.subList(start, end)
    }


    fun searchAndSortFeeds(query: String, feeds: List<Feed>): List<FeedResultDto> {
        val sortedFeeds = sortFeedsByRelevance(feeds, query)
        return sortedFeeds.map { scoredFeed ->
            val feed = scoredFeed.feed
            val folder = feed.folder ?: throw ApplicationException(ErrorCode.NOT_FOUND, "Folder가 존재하지 않습니다. feed ID=${feed.id}")
            FeedResultDto(
                feedId = feed.id!!,
                title = feed.title,
                summary = feed.summary,
                platform = feed.platform ?: "",
                platformImage = findBrunch(feed.platform ?: "").image,
                isMarked = feed.isMarked,
                keywords = feed.keywords.map { it.content },
                folderId = folder.id!!,
                folderName = folder.name
            )
        }
    }

    fun sortFeedsByRelevance(feeds: List<Feed>, query: String): List<ScoredFeedVo> {
        val scoredFeeds = feeds.map { feed ->
            ScoredFeedVo(feed, calculateScore(feed, query))
        }
        return scoredFeeds.sortedWith(
            compareByDescending<ScoredFeedVo> { it.score }
                .thenByDescending { it.feed.createdAt }
        )
    }

    fun calculateScore(feed: Feed, query: String): Double {
        var score = 0.0
        val queryLower = query.lowercase(Locale.getDefault())

        // 1. 제목 유사도 - 제목에서 검색어 등장 횟수에 따라 가중치 부여
        val titleOccurrences = countOccurrences(feed.title, queryLower)
        score += titleOccurrences * 0.6

        // 2. 텍스트 유사도 - 요약에서 검색어 등장 횟수에 따라 가중치 부여
        val summaryOccurrences = countOccurrences(feed.summary, queryLower)
        score += summaryOccurrences * 0.3

        // 3. 키워드 유사도 - 키워드에서 검색어 등장 횟수에 따라 가중치 부여
        val keywordOccurrences = feed.keywords.sumOf { countOccurrences(it.content, queryLower) }
        score += keywordOccurrences * 0.3

        // 4. 메모 유사도 - 메모에서 검색어 등장 횟수에 따라 가중치 부여
        val memoOccurrences = countOccurrences(feed.memo ?: "", queryLower)
        score += memoOccurrences * 0.2

        return score
    }


    // 문자열 내에서 검색어 등장 횟수를 계산하는 함수
    fun countOccurrences(text: String, query: String): Int {
        return Regex(query, RegexOption.IGNORE_CASE).findAll(text).count()
    }

    // 피드 생성
    fun makeFeedAndResponse(content: PromptResponse, brunch: Source, userAccount: UserAccount, link: String): Long {
        val feed = makeFeed(userAccount, content, brunch, link)  // 피드 저장
        createRecommendFolders(feed, content)
        keywordService.createKeywords(feed, content.keyword)
        //return makeAiSummaryResponse(content, brunch, feed.id!!)
        return feed.id!!
    }

    // gemini 요약 결과 업데이트
    @Transactional
    fun updateSummarizedFeed(content: PromptResponse, brunch: Source, feedId: Long, userAccount: UserAccount) {

        val feed = findFeedOrElseThrow(feedId)
        val folder = folderService.getUnclassified(userAccount)

        // 요약 결과 업데이트 (status: COMPLETE 포함)
        feed.updateSummarizedContent(content.summary, content.subject, brunch)
        feed.updateFolder(folder)
        feedRepository.save(feed)

        createRecommendFolders(feed, content)
        keywordService.createKeywords(feed, content.keyword)
    }

    @Transactional
    fun makeFeed(userAccount: UserAccount, content: PromptResponse, brunch: Source, link: String): Feed {
        val user = findUserOrElseThrow(userAccount.userId)
        val folder = folderService.getUnclassified(userAccount)

        // ai 요약 결과로 피드 생성 (유저 매칭을 위해 폴더는 미분류로 지정)
        val feed = Feed(
            folder = folder!!,
            originUrl = link,
            summary = content?.summary ?: "",
            title = content?.subject ?: "",
            platform = brunch.source,
            status = Status.COMPLETED,  // TODO: 워커 이식하면서 수정하기
            isChecked = false,
        )
        return feedRepository.save(feed)
    }

    @Transactional
    fun makeFeedFirst(userId: Long, link: String): Long {
        val user = findUserOrElseThrow(userId)
        val feed = Feed(
            originUrl = link,
            summary = "",
            title =  "",
            platform = "",
            status = Status.REQUESTED,
            isChecked = false,
        )
        return feedRepository.save(feed).id!!
    }

    // 피드 수정
    @Transactional
    fun update(userAccount: UserAccount, request: FeedUpdateReqDto, feedId: Long): FeedUpdateResDto {
        // 유저 확인
        val user = findUserOrElseThrow(userAccount.userId)

        // 기존 폴더 확인 or 새 폴더 생성
        val folder = checkFolder(user, request.folderName)
        folder!!.increaseCount()
        folderRepository.save(folder)

        // 피드 업데이트
        val feed = findFeedOrElseThrow(feedId)
        feed.update(request.title, request.summary, request.memo, folder)
        feed.updateIsChecked()  // 요약 내용 확인 플래그

        // 키워드 업데이트
        updateKeywords(feed, request.keywords)

        // status: SAVED
        feed.updateStatus(Status.SAVED)

        feedRepository.save(feed)
        return FeedUpdateResDto(feed.id!!)
    }

    // 요약 결과 조회 (저장 전)
    @Transactional
    fun getSummaryRes(userAccount: UserAccount, feedId: Long): AiSummaryResponseDto? {

        val feed = findFeedOrElseThrow(feedId)
        val user = findUserOrElseThrow(userAccount.userId)
        val folders = folderRepository.findAllByUser(user)

        return AiSummaryResponseDto(
            feedId = feedId,
            // content = aiSummaryContent,
            platformImage = Source.getImageByName(feed.platform!!)!!,
            recommendFolder = recommendRepository.findRecommendFirst(feedId, 0)?.folderName ?: "",
            recommendFolders = recommendRepository.findRecommendationsByFeedId(feedId)
                ?.map { it.folderName ?: "" }?.ifEmpty { listOf() } ?: emptyList(),
            subject = feed.title ?: "",
            summary = feed.summary ?: "",
            keywords = feed.keywords.stream().map { it.content }.toList() ?: emptyList(),
            folders = folders.map { it -> it.name }.toList(),
            date = feed.createdAt!!.toLocalDate()
        )
    }

    // 요약 중인 링크 조회
    @Transactional
    fun getProcessing(userAccount: UserAccount): ProcessingListDto? {
        val user = findUserOrElseThrow(userAccount.userId)
        val feeds = feedRepository.getProcessing(user)
        var result: MutableList<ProcessingFeedResDto> = mutableListOf()
        for(feed in feeds){
            result.add(
                ProcessingFeedResDto(
                    feedId = feed.id!!,
                    title = feed.title,
                    status = feed.status.toString()
                )
            )
        }
        return ProcessingListDto(processingFeedResDtos = result)
    }

    // 요약 실패 피드 삭제
    @Transactional
    fun deleteProcessingFeed(userAccount: UserAccount, feedId: Long) {
        val user = findUserOrElseThrow(userAccount.userId)
        val feed = findFeedOrElseThrow(feedId)
        if(feed.folder!!.user != user){
            throw ApplicationException(ErrorCode.UNAUTHORIZED, "삭제 권한이 없습니다.")
        }

        feed.updateDeletedAt()
        feedRepository.save(feed)
    }

    @Transactional
    fun updateKeywords(feed: Feed, updatedKeywords: List<String>) {
        // 기존 키워드
        val existingKeywords: MutableList<Keyword> = feed.keywords!!.toMutableList()

        // 기존 키워드 제거
        for (kw in existingKeywords){
            if(!updatedKeywords.any { it == kw.content }){
                keywordRepository.deleteById(kw.id!!)
            }
        }

        // 새로운 키워드 추가
        for(newKw in updatedKeywords){
            if(!existingKeywords.any { it.content == newKw }){
                val newKeyword = Keyword(
                    feed = feed,
                    content = newKw
                )
                keywordRepository.save(newKeyword)
            }
        }
    }

    // 요약 실패 피드 생성
    @Transactional
    fun createFailed(userAccount: UserAccount, link: String) {
        val user = findUserOrElseThrow(userAccount.userId)
        val failedFolder = folderService.getFailed(userAccount)
        val feed = Feed(
            folder = failedFolder,
            originUrl = link,
            summary = "",
            title = "",
            platform = "",
            status = Status.FAILED,
        )
        feedRepository.save(feed)
    }

    @Transactional
    fun createRecommendFolders(feed: Feed, content: PromptResponse) {
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

    @Transactional
    fun createKeywords(feed: Feed, request: FeedUpdateReqDto) {
        val createdKeywords: MutableList<Keyword> = mutableListOf()
        for (keyword in request.keywords) {
            val createdKeyword = Keyword(
                feed = feed,
                content = keyword
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

    fun findBrunch(link: String = ""): Source {
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

    fun findFeedOrElseThrow(feedId: Long): Feed{
        return feedRepository.findById(feedId).orElseThrow {
            ApplicationException(ErrorCode.FEED_NOT_FOUND, "피드를 찾을 수 없습니다.")
        }
    }

    private fun getKeywordsByFeedId(feedId: Long): List<String> {
        val keywords = keywordRepository.findByFeedId(feedId)
        if (keywords.isEmpty()) {
            logger().error("일치하는 feedId에 해당하는 keywords가 없습니다 : $feedId")
            return emptyList()
        }
        return keywords.map { it.content }
    }

    private fun getRecommendFoldersByFeedId(feedId: Long): List<String> {
        val recommendFolders = recommendRepository.findRecommendationsByFeedId(feedId)
        if (recommendFolders.isEmpty()) {
            return emptyList()
        }
        return recommendFolders.map { it.folderName }
    }
}