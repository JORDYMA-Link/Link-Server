package com.jordyma.blink.feed.repository.impl

import com.jordyma.blink.feed.vo.FeedFolderVo
import com.jordyma.blink.feed.repository.CustomFeedRepository
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class CustomFeedRepositoryImpl @Autowired constructor(
    private val queryFactory: JPAQueryFactory
) : CustomFeedRepository {
    override fun findFeedFolderDtoByUserAndDate(userId: Long, date: LocalDate): List<FeedFolderVo> {
        val feed = com.jordyma.blink.feed.entity.QFeed.feed
        val folder = com.jordyma.blink.folder.entity.QFolder.folder
        return queryFactory
            .select(
                Projections.constructor(
                    FeedFolderVo::class.java,
                    folder.id,
                    folder.name,
                    feed
                ))
            .from(feed)
            .join(feed.folder, folder)
            .where(folder.user.id.eq(userId))
            .orderBy(folder.createdAt.desc(), feed.createdAt.desc())
            .fetch()
    }
}