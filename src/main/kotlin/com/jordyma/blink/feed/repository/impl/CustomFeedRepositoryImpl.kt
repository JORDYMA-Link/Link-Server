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
import com.jordyma.blink.folder.entity.QRecommend
import com.jordyma.blink.keyword.entity.QKeyword
import com.jordyma.blink.user.entity.QUser.user
import com.jordyma.blink.user.entity.User
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class CustomFeedRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomFeedRepository {
    override fun findFeedFolderDtoByUserIdAndBetweenDate(
        user: User,
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
                folder.user.id.eq(user.id)
                    .and(feed.createdAt.between(startOfMonth, endOfMonth))
                    .and(feed.deletedAt.isNull)
            )
            .fetch()
    }


    override fun findFeedDetail(user: User, feedId: Long): FeedDetailVo? {
        val qFeed = QFeed.feed
        val qFolder = QFolder.folder

        return queryFactory
            .select(
                Projections.constructor(
                    FeedDetailVo::class.java,
                    qFeed.id,
                    qFeed.thumbnailImageUrl,
                    qFeed.title,
                    qFeed.createdAt.`as`("date"),
                    qFeed.summary,
                    qFeed.platform,
                    qFolder.name.`as`("folderName"),
                    qFeed.memo,
                    qFeed.isMarked,
                    qFeed.originUrl
                )
            )
            .from(qFeed)
            .join(qFolder).on(qFeed.folder.id.eq(qFolder.id))
            .where(
                qFolder.user.id.eq(user.id)
                    .and(qFeed.id.eq(feedId))
                    .and(qFeed.deletedAt.isNull)
            )
            .fetchOne()
    }

    override fun deleteAllByFolder(folder: Folder): Long {
        return queryFactory
            .update(QFeed.feed)
            .where(QFeed.feed.folder.eq(folder))
            .set(QFeed.feed.deletedAt, LocalDateTime.now())
            .execute()
    }

    override fun deleteKeywords(folder: Folder): Long {
        return queryFactory
            .update(QKeyword.keyword)
            .where(QKeyword.keyword.feed.folder.eq(folder))
            .set(QKeyword.keyword.deletedAt, LocalDateTime.now())
            .execute()
    }

    override fun deleteRecommend(folder: Folder): Long {
        return queryFactory
            .update(QRecommend.recommend)
            .where(QRecommend.recommend.feed.folder.eq(folder))
            .set(QRecommend.recommend.deletedAt, LocalDateTime.now())
            .execute()
    }

    override fun findAllByFolder(folder: Folder, cursor: Int?, pageSize: Long): List<Feed> {
        val condition = BooleanBuilder()

        if (cursor != null) {
            condition.and(feed.id.lt(cursor.toLong()))
        }

        // TODO 쿼리 튜닝 필요 (N+1 문제)
        return queryFactory
            .select(feed)
            .from(feed)
            .join(feed.folder, QFolder.folder)
            .fetchJoin()
            .where(
                condition.and(feed.folder.eq(folder)),
                feed.deletedAt.isNull
            )
            .limit(pageSize)
            .orderBy(feed.id.desc())
            .fetch()
    }

    override fun getProcessing(findUser: User): List<Feed> {
        return queryFactory
            .selectFrom(feed)
            .join(feed.folder, folder).fetchJoin()
            .join(folder.user, user).fetchJoin()
            .where(
                feed.folder.user.eq(findUser).and(
                    feed.status.eq(Status.PROCESSING)
                        .or(feed.status.eq(Status.COMPLETED).and(feed.isChecked.isFalse))
                        .or(feed.status.eq(Status.REQUESTED))
                        .or(feed.status.eq(Status.FAILED).and(feed.deletedAt.isNull))
                )
            )
            .fetch()
    }
}