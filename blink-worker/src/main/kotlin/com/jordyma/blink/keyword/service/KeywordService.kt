package com.jordyma.blink.keyword.service
import com.jordyma.blink.feed.domain.Feed
import com.jordyma.blink.feed.domain.FeedRepository
import com.jordyma.blink.keyword.Keyword
import com.jordyma.blink.keyword.KeywordRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class KeywordService(
    private val keywordRepository: KeywordRepository,
    private val feedRepository: FeedRepository
){
    @Transactional
    fun createKeywords(feed: Feed, keywords: List<String>) {
        val createdKeywords: MutableList<Keyword> = mutableListOf()
        for (keyword in keywords) {
            val createdKeyword = Keyword(
                feed = feed,
                content = keyword
            )
            keywordRepository.save(createdKeyword)
        }
        feed.keywords = createdKeywords
        feedRepository.save(feed)
    }
}