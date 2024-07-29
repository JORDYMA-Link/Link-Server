package com.jordyma.blink.feed.repository.impl

import com.jordyma.blink.feed.entity.Feed
import com.jordyma.blink.feed.entity.QFeed
import com.jordyma.blink.feed.vo.FeedFolderVo
import com.jordyma.blink.feed.repository.CustomFeedRepository
import com.jordyma.blink.folder.entity.Folder
import com.jordyma.blink.folder.entity.QFolder
import com.jordyma.blink.keyword.entity.QKeyword
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime

@Repository
class CustomFeedRepositoryImpl(
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

    override fun deleteAllByFolder(folder: Folder) {
        queryFactory
            .delete(QFeed.feed)
            .where(QFeed.feed.folder.eq(folder))
            .execute()
    }

    override fun findAllByFolder(folder: Folder): List<Feed> {
        return queryFactory
            .select(QFeed.feed)
            .from(QFeed.feed)
            .join(QFeed.feed.folder, QFolder.folder)
            .fetchJoin()
            .join(QKeyword.keyword1.feed, QFeed.feed)
            .where(QFeed.feed.folder.eq(folder))
            .fetch()
    }
}