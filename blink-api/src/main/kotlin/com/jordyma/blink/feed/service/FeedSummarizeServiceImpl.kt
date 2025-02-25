package com.jordyma.blink.feed.service

import com.jordyma.blink.common.system.CommonParameterCode.EXCEPTION_LINK_PARAM_CODE
import com.jordyma.blink.common.system.CommonParameterRepository
import com.jordyma.blink.feed.domain.Feed
import com.jordyma.blink.feed.domain.FeedRepository
import com.jordyma.blink.feed.domain.Source
import com.jordyma.blink.feed.domain.service.FeedSummarizeService
import com.jordyma.blink.feed.domain.service.PromptResponse
import com.jordyma.blink.folder.domain.service.FolderService
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.keyword.service.KeywordService
import com.jordyma.blink.logger
import com.jordyma.blink.recommend.Recommend
import com.jordyma.blink.recommend.RecommendRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FeedSummarizeServiceImpl(
    private val commonParamRepository: CommonParameterRepository,
    private val feedRepository: FeedRepository,
    private val folderService: FolderService,
    private val recommendRepository: RecommendRepository,
    private val keywordService: KeywordService,
) : FeedSummarizeService {

    private lateinit var cachedInvalidLinks: List<String>

    @Transactional
    override fun updateSummarizedFeed(
        content: PromptResponse,
        brunch: Source,
        feedId: Long,
        userId: Long,
        thumbnailImage: String
    ): Feed {
        logger().info(">>>>> feed update start")

        val feed = findFeedOrElseThrow(feedId)
        val folder = folderService.getUnclassified(userId)

        // 요약 결과 업데이트 (status: COMPLETE 포함)
        feed.updateSummarizedContent(content.summary, content.subject, brunch)
        feed.updateFolder(folder)
        feed.updateThumbnailImageUrl(thumbnailImage)
        feedRepository.save(feed)

        logger().info("요약 결과 업데이트 성공")

        createRecommendFolders(feed, content.category)
        keywordService.createKeywords(feed, content.keyword)
        return feed
    }

    override fun createRecommendFolders(feed: Feed, category: List<String>) {
        var cnt = 0
        val recommendFolders: MutableList<Recommend> = mutableListOf()
        for (folderName in category) {
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

    private fun findBrunch(link: String): Source {
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
        } else if (link.contains("youtube.com")) {
            Source.YOUTUBE
        } else if (link.contains("naver.com")) {
            Source.NAVER
        } else if (link.contains("google.com")) {
            Source.GOOGLE
        } else {
            Source.DEFAULT
        }
    }

    private fun findFeedOrElseThrow(feedId: Long): Feed {
        return feedRepository.findById(feedId).orElseThrow {
            ApplicationException(ErrorCode.FEED_NOT_FOUND, "피드를 찾을 수 없습니다.")
        }
    }

    companion object{
        const val SUMMARY_COMPLETED = "링크 요약이 완료되었어요."
    }

    @PostConstruct
    fun loadInvalidLinks() {
        cachedInvalidLinks = commonParamRepository.findByParamCode(EXCEPTION_LINK_PARAM_CODE).map { it.paramValue }
        logger().info(">>>>> cachedInvalidLinks: $cachedInvalidLinks")
    }

    fun isInvalidLink(link: String): Boolean {
        return cachedInvalidLinks.contains(link)
    }
}
