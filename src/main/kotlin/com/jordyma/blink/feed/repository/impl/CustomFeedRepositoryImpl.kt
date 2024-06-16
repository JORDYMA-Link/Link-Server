package com.jordyma.blink.feed.repository.impl

import com.jordyma.blink.feed.entity.QFeed
import com.jordyma.blink.feed.vo.FeedFolderVo
import com.jordyma.blink.feed.repository.CustomFeedRepository
import com.jordyma.blink.folder.entity.QFolder
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime

@Repository
class CustomFeedRepositoryImpl @Autowired constructor(
    private val queryFactory: JPAQueryFactory
) : CustomFeedRepository {
    override fun findFeedFolderDtoByUserIdAndBetweenDate(
        userId: Long,
        startOfMonth: LocalDateTime,
        endOfMonth: LocalDateTime
    ): List<FeedFolderVo> {
        val feed = QFeed.feed
        val folder = QFolder.folder

        return queryFactory
            .select(
                Projections.constructor(
                    FeedFolderVo::class.java,
                    folder.id,
                    folder.name,
                    feed
                )
            )
            .from(feed)
            .join(feed.folder, folder)
            .where(
                folder.user.id.eq(userId)
                    .and(feed.createdAt.between(startOfMonth, endOfMonth))
            )
            .fetch()
    }
}