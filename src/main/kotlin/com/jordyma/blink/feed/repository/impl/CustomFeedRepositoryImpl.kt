package com.jordyma.blink.feed.repository.impl

import com.jordyma.blink.feed.entity.Feed
import com.jordyma.blink.feed.entity.QFeed
import com.jordyma.blink.feed.entity.QFeed.feed
import com.jordyma.blink.feed.entity.Status
import com.jordyma.blink.feed.vo.FeedFolderVo
import com.jordyma.blink.feed.repository.CustomFeedRepository
import com.jordyma.blink.feed.vo.FeedDetailVo
import com.jordyma.blink.folder.entity.Folder
import com.jordyma.blink.folder.entity.QFolder
import com.jordyma.blink.folder.entity.QFolder.folder
import com.jordyma.blink.keyword.entity.QKeyword
import com.jordyma.blink.user.entity.QUser.user
import com.jordyma.blink.user.entity.User
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
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


    override fun findFeedDetail(memberId: Long, feedId: Long): FeedDetailVo? {
        val qFeed = QFeed.feed
        val qFolder = QFolder.folder

        return queryFactory
            .select(
                Projections.constructor(
                    FeedDetailVo::class.java,
                    qFeed.id,
                    qFeed.thumbnailImage,
                    qFeed.title,
                    qFeed.createdAt.`as`("date"),
                    qFeed.summary,
                    qFolder.name.`as`("folderName"),
                    qFeed.memo
                )
            )
            .from(qFeed)
            .join(qFolder).on(qFeed.folder.id.eq(qFolder.id))
            .where(
                qFolder.user.id.eq(memberId),
                qFeed.id.eq(feedId)
            )
            .fetchOne()
    }

    override fun deleteAllByFolder(folder: Folder): Long {
        return queryFactory
            .delete(QFeed.feed)
            .where(QFeed.feed.folder.eq(folder))
            .execute()
    }

    override fun findAllByFolder(folder: Folder): List<Feed> {
        return queryFactory
            .select(QFeed.feed)
            .from(QFeed.feed)
            .join(QFeed.feed.folder, QFolder.folder)
            .join(QFeed.feed.keywords, QKeyword.keyword)
            .fetchJoin()
            .where(QFeed.feed.folder.eq(folder))
            .fetch()
    }

    override fun getProcessing(findUser: User): List<Feed> {
        return queryFactory
            .selectFrom(feed)
            .join(feed.folder, folder).fetchJoin()
            .join(folder.user, user).fetchJoin()
            .where(
                ((feed.status.eq(Status.PROCESSING)).and(feed.folder.user.eq(user)))
                    .or((feed.status.eq(Status.COMPLETED).and(feed.isChecked.isFalse))
                        .and(feed.folder.user.eq(user)))
                    .or((feed.status.eq(Status.REQUESTED)
                        .and(feed.folder.user.eq(user)))
                    .or((feed.status.eq(Status.FAILED).and(feed.deletedAt.isNotNull))
                        .and(feed.folder.user.eq(user)))))
            .fetch()
    }
}