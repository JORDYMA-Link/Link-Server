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
import com.jordyma.blink.folder.entity.QRecommend
import com.jordyma.blink.keyword.entity.QKeyword
import com.jordyma.blink.user.entity.QUser.user
import com.jordyma.blink.user.entity.User
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class FeedRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : FeedRepositoryCustom {
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
                    qFeed.id.`as`("feedId"),
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
                (feed.deletedAt.isNull)
                    .and(((feed.status.eq(Status.PROCESSING)).and(feed.folder.user.eq(user)))
                    .or((feed.status.eq(Status.COMPLETED).and(feed.isChecked.isFalse))
                        .and(feed.folder.user.eq(user)))
                    .or((feed.status.eq(Status.REQUESTED)
                        .and(feed.folder.user.eq(user)))
                        .or((feed.status.eq(Status.FAILED))
                            .and(feed.folder.user.eq(user))))))
            .fetch()
    }


    override fun findUnclassifiedFeeds(userId: Long, pageable: Pageable): Page<Feed> {
        val feed = QFeed.feed
        val folder = QFolder.folder

        // 기본 쿼리 작성
        val query = queryFactory
            .selectFrom(feed)
            .join(feed.folder, folder)
            .where(
                folder.user.id.eq(userId)
                    .and(folder.isUnclassified.isTrue)
                    .and(feed.status.eq(Status.COMPLETED)) // 상태가 ENUM인 경우
                    .and(feed.deletedAt.isNull)
            )

        // 페이징 적용 및 결과 가져오기
        val feeds = query
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        // 총 개수 조회 및 Page 객체 생성
        val total = queryFactory
            .select(feed.count())
            .from(feed)
            .join(feed.folder, folder)
            .where(
                folder.user.id.eq(userId)
                    .and(folder.isUnclassified.isTrue)
                    .and(feed.status.eq(Status.COMPLETED))
                    .and(feed.deletedAt.isNull)
            )
            .fetchOne() ?: 0

        return PageableExecutionUtils.getPage(feeds, pageable) { total }
    }

    override fun findBookmarkedFeeds(userId: Long, pageable: Pageable): Page<Feed> {
        val feed = QFeed.feed
        val folder = QFolder.folder

        // 기본 쿼리 작성
        val query = queryFactory
            .selectFrom(feed)
            .join(feed.folder, folder)
            .where(
                folder.user.id.eq(userId)
                    .and(feed.isMarked.isTrue)
                    .and(feed.status.eq(Status.COMPLETED)) // status가 Enum인 경우
                    .and(feed.deletedAt.isNull)
            )

        // 페이징 적용 및 결과 가져오기
        val feeds = query
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        // 총 개수 조회 및 Page 객체 생성
        val total = queryFactory.select(feed.count())
            .from(feed)
            .join(feed.folder, folder)
            .where(
                folder.user.id.eq(userId)
                    .and(feed.isMarked.isTrue)
                    .and(feed.status.eq(Status.COMPLETED))
                    .and(feed.deletedAt.isNull)
            )
            .fetchOne() ?: 0

        return PageableExecutionUtils.getPage(feeds, pageable) { total }
    }

    override fun findFeedByQuery(userId: Long, query: String, pageable: Pageable): Page<Feed> {
        val feed = QFeed.feed
        val keyword = QKeyword.keyword
        val folder = QFolder.folder

        // 기본 쿼리 작성
        val jpaQuery = queryFactory
            .selectFrom(feed)
            .distinct() // 중복 제거를 위해 distinct 사용
            .join(feed.keywords, keyword)
            .join(feed.folder, folder)
            .where(
                folder.user.id.eq(userId)
                    .and(feed.folder.id.eq(folder.id))
                    .and(keyword.feed.id.eq(feed.id))
                    .and(feed.deletedAt.isNull)
                    .and(
                        feed.title.lower().like("%${query.lowercase()}%")
                            .or(feed.summary.lower().like("%${query.lowercase()}%"))
                            .or(feed.memo.lower().like("%${query.lowercase()}%"))
                            .or(keyword.content.lower().like("%${query.lowercase()}%"))
                    )

            )

        // 페이징 적용 및 결과 가져오기
        val feeds = jpaQuery
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        // 총 개수 조회 (페이징 최적화를 위해 카운트 쿼리 재사용 가능)
        val countQuery = queryFactory
            .select(feed.countDistinct())
            .from(feed)
            .join(feed.keywords, keyword)
            .join(feed.folder, folder)
            .where(
                folder.user.id.eq(userId)
                    .and(feed.folder.id.eq(folder.id))
                    .and(keyword.feed.id.eq(feed.id))
                    .and(feed.deletedAt.isNull)
                    .and(
                        feed.title.lower().like("%${query.lowercase()}%")
                            .or(feed.summary.lower().like("%${query.lowercase()}%"))
                            .or(feed.memo.lower().like("%${query.lowercase()}%"))
                            .or(keyword.content.lower().like("%${query.lowercase()}%"))
                    )
            )

        return PageableExecutionUtils.getPage(feeds, pageable) { countQuery.fetchOne() ?: 0 }

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

}