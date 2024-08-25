package com.jordyma.blink.feed.repository.impl

import com.jordyma.blink.feed.entity.Feed
import com.jordyma.blink.feed.entity.QFeed
import com.jordyma.blink.feed.entity.QFeed.feed
import com.jordyma.blink.feed.entity.Status
import com.jordyma.blink.feed.repository.FeedRepositoryCustom
import com.jordyma.blink.feed.vo.FeedFolderVo
import com.jordyma.blink.feed.vo.FeedDetailVo
import com.jordyma.blink.folder.entity.Folder
import com.jordyma.blink.folder.entity.QFolder
import com.jordyma.blink.folder.entity.QFolder.folder
import com.jordyma.blink.keyword.entity.QKeyword
import com.jordyma.blink.user.entity.QUser.user
import com.jordyma.blink.user.entity.User
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class FeedRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : FeedRepositoryCustom {
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
                    .and(feed.createdAt.between(startOfMonth, endOfMonth)),
                feed.deletedAt.isNull
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
            .update(QFeed.feed)
            .where(QFeed.feed.folder.eq(folder))
            .set(QFeed.feed.deletedAt, LocalDateTime.now())
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