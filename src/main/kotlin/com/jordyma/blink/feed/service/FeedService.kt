package com.jordyma.blink.feed.service

import com.jordyma.blink.feed.dto.*
import com.jordyma.blink.feed.entity.Feed
import com.jordyma.blink.global.util.rangeTo
import com.jordyma.blink.feed.repository.FeedRepository
import com.jordyma.blink.feed.vo.ScoredFeedVo
import com.jordyma.blink.global.error.ID_NOT_FOUND
import com.jordyma.blink.global.error.exception.IdRequiredException
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.global.util.DateTimeUtils
import com.jordyma.blink.global.util.DateTimeUtils.localDateTimeToString
import com.jordyma.blink.global.util.DateTimeUtils.localDateTimeToStringDefault
import com.jordyma.blink.keyword.repository.KeywordRepository
import com.jordyma.blink.user.dto.UserInfoDto
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.min

@Service
class FeedService(
    private val feedRepository: FeedRepository,
    private val keywordRepository: KeywordRepository,
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
                FeedItemDto(
                    folderId = feedFolderDto.folderId,
                    folderName = feedFolderDto.folderName,
                    feedId = feedFolderDto.feed.id ?: throw IdRequiredException(ID_NOT_FOUND),
                    title = feedFolderDto.feed.title,
                    summary = feedFolderDto.feed.summary,
                    platform = feedFolderDto.feed.platform,
                    platformImage = feedFolderDto.feed.platformImage,
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

    @Throws(ApplicationException::class)
    @Transactional(readOnly = true)
    fun getFeedDetail(user: UserInfoDto, feedId: Long): FeedDetailDto {
        val feedDetail = feedRepository.findFeedDetail(user.id, feedId)
            ?: throw ApplicationException(ErrorCode.NOT_FOUND, "일치하는 feedId가 없습니다 : $feedId", Throwable())
        return FeedDetailDto(
            feedId = feedDetail.feedId,
            thumnailImage = feedDetail.thumnailImage,
            platformImage = feedDetail.platformImage,
            title = feedDetail.title,
            date = localDateTimeToString(feedDetail.date),
            summary = feedDetail.summary,
            keywords = getKeywordsByFeedId(feedId), // 키워드 추출 함수
            folderName = feedDetail.folderName,
            memo = feedDetail.memo,
            isMarked = feedDetail.isMarked,
            originUrl = feedDetail.originUrl
        )
    }


    private fun getKeywordsByFeedId(feedId: Long): List<String> {
        val keywords = keywordRepository.findByFeedId(feedId)
        if (keywords.isEmpty()) {
            throw ApplicationException(ErrorCode.NOT_FOUND, "일치하는 feedId에 해당하는 keywords가 없습니다 : $feedId", Throwable())
        }
        return keywords.map { it.content }
    }


    @Transactional
    fun deleteFeed(user: UserInfoDto, feedId: Long) {
        val feed = feedRepository.findById(feedId)
            .orElseThrow { ApplicationException(ErrorCode.NOT_FOUND, "일치하는 feedId가 없습니다 : $feedId", Throwable()) }
        if (feed.folder.user.id != user.id) {
            throw ApplicationException(ErrorCode.FORBIDDEN, "해당 피드를 삭제할 권한이 없습니다", Throwable())
        }
        feed.modifyDeletedDate(LocalDateTime.now())
        feedRepository.save(feed)
    }

    @Transactional
    fun changeIsMarked(user: UserInfoDto, feedId: Long, setMarked: Boolean): FeedIsMarkedResponseDto {
        val feed = feedRepository.findById(feedId)
            .orElseThrow { ApplicationException(ErrorCode.NOT_FOUND, "일치하는 feedId가 없습니다 : $feedId", Throwable()) }
        if (feed.folder.user.id != user.id) {
            throw ApplicationException(ErrorCode.FORBIDDEN, "해당 피드를 수정할 권한이 없습니다", Throwable())
        }
        feed.changeIsMarked(setMarked)
        feed.modifyUpdatedDate(LocalDateTime.now())
        feedRepository.save(feed)

        val newFeed = getFeed(feedId)
        return FeedIsMarkedResponseDto(
            id = newFeed.id ?: throw IdRequiredException(ID_NOT_FOUND),
            isMarked = newFeed.isMarked,
            modifiedDate = if (newFeed.updatedAt != null) DateTimeUtils.localDateTimeToString(newFeed.updatedAt!!) else "9999-12-31"
        )
    }

    @Transactional(readOnly = true)
    fun getFeed(feedId: Long): Feed
            = feedRepository.findById(feedId)
        .orElseThrow { ApplicationException(ErrorCode.NOT_FOUND, "일치하는 feedId가 없습니다 : $feedId", Throwable()) }

    @Transactional(readOnly = true)
    fun getFeedsByType(user: UserInfoDto, type: FeedType, page: Int, size: Int): List<FeedTypeResponseDto> {
        val pageable = PageRequest.of(page, size)
        val feedList =  when (type) {
            FeedType.BOOKMARKED -> feedRepository.findBookmarkedFeeds(user.id, pageable).content
            FeedType.UNCLASSIFIED -> feedRepository.findUnclassifiedFeeds(user.id, pageable).content
        }
        val recommendedFolderList: List<String>? = null // todo recommendedRepository.findRecommendedFolder(feedId)
        return feedList.map { feed ->
            FeedTypeResponseDto(
                feedId = feed.id!!,
                title = feed.title,
                summary = feed.summary,
                platform = feed.platform,
                platformImage = feed.platformImage,
                isMarked = feed.isMarked,
                keywords = getKeywordsByFeedId(feed.id), // 키워드 추출 함수
                recommendedFolder = recommendedFolderList
            )
        }
    }

    @Transactional(readOnly = true)
    fun searchFeeds(user: UserInfoDto, query: String, page: Int, size: Int): List<FeedResultDto> {
        val fetchSize = size * 5 // 5배로 fetch
        val pageable = PageRequest.of(page / 5, fetchSize) // 실제 페이징 계산 (page/5)
        val feedList = feedRepository.findFeedByQuery(user.id, query, pageable).content

        // DB에서 가져온 데이터를 가중치에 따라 정렬
        val sortedFeeds = searchAndSortFeeds(query, feedList)

        // 클라이언트에서 요청한 데이터만큼만 반환
        val start = (page % 5) * size  // 클라이언트가 요청한 페이지의 시작 인덱스
        val end = min(start + size, sortedFeeds.size) // 끝 인덱스는 정렬된 데이터 크기 내로 제한
        return sortedFeeds.subList(start, end)
    }


    fun searchAndSortFeeds(query: String, feeds: List<Feed>): List<FeedResultDto> {
        val sortedFeeds = sortFeedsByRelevance(feeds, query)
        return sortedFeeds.map { scoredFeed ->
            val feed = scoredFeed.feed
            FeedResultDto(
                feedId = feed.id!!,
                title = feed.title,
                summary = feed.summary,
                platform = feed.platform,
                platformImage = feed.platformImage,
                isMarked = feed.isMarked,
                keywords = feed.keywords.map { it.content },
                date = localDateTimeToStringDefault(feed.createdAt?: LocalDateTime.now())
            )
        }.sortedByDescending { it.date }
    }

    fun sortFeedsByRelevance(feeds: List<Feed>, query: String): List<ScoredFeedVo> {
        val scoredFeeds = feeds.map { feed ->
            ScoredFeedVo(feed, calculateScore(feed, query))
        }
        return scoredFeeds.sortedByDescending { it.score }
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

        // 3. 텍스트 유사도 - 키워드에서 검색어 등장 횟수에 따라 가중치 부여
        val keywordOccurrences = feed.keywords.sumOf { countOccurrences(it.content, queryLower) }
        score += keywordOccurrences * 0.3

        // 4. 메모 유사도 - 메모에서 검색어 등장 횟수에 따라 가중치 부여
        val memoOccurrences = countOccurrences(feed.memo, queryLower)
        score += memoOccurrences * 0.2

        return score
    }


    // 문자열 내에서 검색어 등장 횟수를 계산하는 함수
    fun countOccurrences(text: String, query: String): Int {
        return Regex(query, RegexOption.IGNORE_CASE).findAll(text).count()
    }

}
