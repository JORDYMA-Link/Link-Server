package com.jordyma.blink.feed.service

import com.jordyma.blink.auth.jwt.user_account.UserAccount
import com.jordyma.blink.feed.dto.FeedCalendarListDto
import com.jordyma.blink.global.error.KEYWORDS_NOT_FOUND
import com.jordyma.blink.global.error.exception.BadRequestException
import com.jordyma.blink.global.util.rangeTo
import com.jordyma.blink.feed.dto.FeedCalendarResponseDto
import com.jordyma.blink.feed.dto.FeedDto
import com.jordyma.blink.feed.dto.request.FeedCreateReqDto
import com.jordyma.blink.feed.dto.response.FeedCreateResDto
import com.jordyma.blink.feed.entity.Brunch
import com.jordyma.blink.feed.entity.Feed
import com.jordyma.blink.feed.repository.FeedRepository
import com.jordyma.blink.folder.entity.Folder
import com.jordyma.blink.folder.repository.FolderRepository
import com.jordyma.blink.folder.service.FolderService
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
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
            source = brunch.brunch,
            // sourceUrl = brunch.image,
            // thumbnailImage = "",
        )
        feedRepository.save(feed)

        // 키워드 생성
        val createdKeywords: MutableList<Keyword> = mutableListOf()
        for(keyword in request.keywords){
            val createdKeyword = Keyword(
                feed = feed,
                keyword = keyword
            )
            keywordRepository.save(createdKeyword)
            createdKeywords.add(createdKeyword)
        }
        feed.updateKeywords(createdKeywords)

        if(feed.id == null){
            throw ApplicationException(ErrorCode.NOT_CREATED, "피드 생성 오류")
        }
        return FeedCreateResDto(feed.id)
    }

    fun checkFolder(user: User, folderName: String): Folder? {
        var folder = folderRepository.findAllByUser(user).firstOrNull { it.name == folderName }
        if(folder == null){
            folder = Folder(
                name = folderName,
                user = user,
                count = 0,
            )
        }
        return folder
    }

    fun findBrunch(link: String): Brunch {
        return if(link.contains("blog.naver.com")){
            Brunch.NAVER_BLOG
        } else if (link.contains("velog.io")){
            Brunch.VELOG
        } else if (link.contains("brunch.co.kr")){
            Brunch.BRUNCH
        } else if (link.contains("yozm.wishket")){
            Brunch.YOZM_IT
        } else if (link.contains("tistory.com")){
            Brunch.TISTORY
        } else if (link.contains("eopla.net")){
            Brunch.EO
        } else{
            Brunch.DEFAULT
        }
    }

    fun findUserOrElseThrow(userId: Long): User {
        return userRepository.findById(userId).orElseThrow {
            ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.")
        }
    }
}
