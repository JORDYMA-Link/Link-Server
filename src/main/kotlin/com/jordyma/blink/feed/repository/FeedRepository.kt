package com.jordyma.blink.feed.repository

import com.jordyma.blink.feed.entity.Feed
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface FeedRepository : JpaRepository<Feed, Long>, FeedRepositoryCustom {

    @Query(
        "SELECT fd FROM Feed fd JOIN Folder fdr ON fd.folder = fdr " +
         "WHERE fdr.user.id = :userId "+
           "AND fd.isMarked = true " +
           "AND fd.status = 'COMPLETED'" +
           "AND fd.deletedAt IS NULL"
    )
    fun findBookmarkedFeeds(userId: Long, pageable: Pageable): Page<Feed>

    @Query(
        "SELECT fd FROM Feed fd JOIN Folder fdr ON fd.folder = fdr " +
         "WHERE fdr.user.id = :userId " +
           "AND fdr.isUnclassified = true " +
           "AND fd.status = 'COMPLETED'"+
           "AND fd.deletedAt IS NULL"
    )
    fun findUnclassifiedFeeds(userId: Long, pageable: Pageable): Page<Feed>

    @Query(
        "SELECT f FROM Feed f " +
          "JOIN f.keywords k " +
          "JOIN f.folder fo " +
         "WHERE fo.user.id = :userId " +  // userId로 필터링
           "AND f.folder.id = fo.id " +  // feed와 folder 연결
           "AND k.feed.id = f.id " +  // keyword와 feed 연결
           "AND (LOWER(f.title) LIKE LOWER(CONCAT('%', :query, '%')) " +  // title에 LIKE 검색
            "OR LOWER(f.summary) LIKE LOWER(CONCAT('%', :query, '%')) " +  // summary에 LIKE 검색
            "OR LOWER(f.memo) LIKE LOWER(CONCAT('%', :query, '%')) " +  // memo에 LIKE 검색
            "OR LOWER(k.content) LIKE LOWER(CONCAT('%', :query, '%')))" +// keyword에 LIKE 검색
           "AND f.deletedAt IS NULL"
    )
    fun findFeedByQuery(
        @Param("userId") userId: Long,
        @Param("query") query: String,
        pageable: Pageable
    ): Page<Feed>

}

