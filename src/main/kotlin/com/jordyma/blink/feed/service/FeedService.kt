package com.jordyma.blink.feed.service

import com.jordyma.blink.common.error.KEYWORDS_NOT_FOUND
import com.jordyma.blink.common.error.exception.BadRequestException
import com.jordyma.blink.common.util.DateTimeUtils
import com.jordyma.blink.feed.dto.FeedDateResponseDto
import com.jordyma.blink.feed.dto.FeedItem
import com.jordyma.blink.feed.dto.FeedsInFolder
import com.jordyma.blink.feed.repository.FeedRepository
import com.jordyma.blink.keyword.repository.KeywordRepository
import com.jordyma.blink.user.entity.User
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class FeedService constructor(
    @Autowired private val feedRepository: FeedRepository,
    @Autowired private val keywordRepository: KeywordRepository,
) {
    @Transactional
    fun getFeedsByDate(date: LocalDate, user: User): FeedDateResponseDto {
        val feeds = feedRepository.findFeedFolderDtoByUserAndDate(user.id, date)
        val groupedFeeds = feeds.groupBy { it.folderId }
        val feedsInFolderList = groupedFeeds.map { (folderId, feedsInFolder) ->
            FeedsInFolder(
                folderId = folderId,
                folderName = feedsInFolder.first().folderName,
                folderCount = feedsInFolder.size,
                feedList = feedsInFolder.map {
                    FeedItem(
                        feedId = it.feed.id,
                        title = it.feed.title,
                        summary = it.feed.summary,
                        source = it.feed.source,
                        sourceUrl = it.feed.sourceUrl,
                        isMarked = it.feed.isMarked,
                        keywords = getKeywordsByFeedId(it.feed.id)
                    )
                }
            )
        }
        return FeedDateResponseDto(
            date = DateTimeUtils.localDateToString(date),
            count = feeds.size,
            feedsInFolderList = feedsInFolderList
        )
    }

    private fun getKeywordsByFeedId(feedId: Long): List<String> {
        val keywords = keywordRepository.findByFeedId(feedId)
        if (keywords.isEmpty()) {
            throw BadRequestException(KEYWORDS_NOT_FOUND)
        }
        return keywords.map { it.keyword }
    }


}
