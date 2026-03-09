package com.jordyma.blink.feed.domain;

@org.springframework.stereotype.Repository()
@jakarta.transaction.Transactional()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\b\u0017\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\t\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\n\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J-\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f2\u0006\u0010\u0007\u001a\u00020\b2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0010\u001a\u00020\u0006H\u0016\u00a2\u0006\u0002\u0010\u0011J\u001e\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\r0\u00132\u0006\u0010\u0014\u001a\u00020\u00062\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u001e\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\r0\u00132\u0006\u0010\u0014\u001a\u00020\u00062\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J&\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\r0\u00132\u0006\u0010\u0014\u001a\u00020\u00062\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u001a\u0010\u001b\u001a\u0004\u0018\u00010\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u0006H\u0016J&\u0010 \u001a\b\u0012\u0004\u0012\u00020!0\f2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020#H\u0016J&\u0010 \u001a\b\u0012\u0004\u0012\u00020!0\f2\u0006\u0010\u0014\u001a\u00020\u00062\u0006\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020#H\u0016J\u001e\u0010%\u001a\b\u0012\u0004\u0012\u00020\r0\u00132\u0006\u0010\u0014\u001a\u00020\u00062\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J&\u0010&\u001a\b\u0012\u0004\u0012\u00020\r0\f2\u0006\u0010\'\u001a\u00020#2\u0006\u0010(\u001a\u00020#2\u0006\u0010\u0014\u001a\u00020\u0006H\u0016J\u0016\u0010)\u001a\b\u0012\u0004\u0012\u00020\r0\f2\u0006\u0010*\u001a\u00020\u001eH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0092\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006+"}, d2 = {"Lcom/jordyma/blink/feed/domain/FeedRepositoryCustomImpl;", "Lcom/jordyma/blink/feed/domain/FeedRepositoryCustom;", "queryFactory", "Lcom/querydsl/jpa/impl/JPAQueryFactory;", "(Lcom/querydsl/jpa/impl/JPAQueryFactory;)V", "deleteAllByFolder", "", "folder", "Lcom/jordyma/blink/folder/Folder;", "deleteKeywords", "deleteRecommend", "findAllByFolder", "", "Lcom/jordyma/blink/feed/domain/Feed;", "cursor", "", "pageSize", "(Lcom/jordyma/blink/folder/Folder;Ljava/lang/Integer;J)Ljava/util/List;", "findAllByUser", "Lorg/springframework/data/domain/Page;", "userId", "pageable", "Lorg/springframework/data/domain/Pageable;", "findBookmarkedFeeds", "findFeedByQuery", "query", "", "findFeedDetail", "Lcom/jordyma/blink/feed/vo/FeedDetailVo;", "user", "Lcom/jordyma/blink/user/User;", "feedId", "findFeedFolderDtoByUserIdAndBetweenDate", "Lcom/jordyma/blink/feed/vo/FeedFolderVo;", "startOfMonth", "Ljava/time/LocalDateTime;", "endOfMonth", "findUnclassifiedFeeds", "getFeedBetween", "startDate", "endDate", "getProcessing", "findUser", "blink-core"})
public class FeedRepositoryCustomImpl implements com.jordyma.blink.feed.domain.FeedRepositoryCustom {
    @org.jetbrains.annotations.NotNull()
    private final com.querydsl.jpa.impl.JPAQueryFactory queryFactory = null;
    
    public FeedRepositoryCustomImpl(@org.jetbrains.annotations.NotNull()
    com.querydsl.jpa.impl.JPAQueryFactory queryFactory) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.util.List<com.jordyma.blink.feed.vo.FeedFolderVo> findFeedFolderDtoByUserIdAndBetweenDate(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.user.User user, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime startOfMonth, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime endOfMonth) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.util.List<com.jordyma.blink.feed.vo.FeedFolderVo> findFeedFolderDtoByUserIdAndBetweenDate(long userId, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime startOfMonth, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime endOfMonth) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public com.jordyma.blink.feed.vo.FeedDetailVo findFeedDetail(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.user.User user, long feedId) {
        return null;
    }
    
    @java.lang.Override()
    public long deleteAllByFolder(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.folder.Folder folder) {
        return 0L;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.util.List<com.jordyma.blink.feed.domain.Feed> findAllByFolder(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.folder.Folder folder, @org.jetbrains.annotations.Nullable()
    java.lang.Integer cursor, long pageSize) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public org.springframework.data.domain.Page<com.jordyma.blink.feed.domain.Feed> findAllByUser(long userId, @org.jetbrains.annotations.NotNull()
    org.springframework.data.domain.Pageable pageable) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.util.List<com.jordyma.blink.feed.domain.Feed> getProcessing(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.user.User findUser) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public org.springframework.data.domain.Page<com.jordyma.blink.feed.domain.Feed> findUnclassifiedFeeds(long userId, @org.jetbrains.annotations.NotNull()
    org.springframework.data.domain.Pageable pageable) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public org.springframework.data.domain.Page<com.jordyma.blink.feed.domain.Feed> findBookmarkedFeeds(long userId, @org.jetbrains.annotations.NotNull()
    org.springframework.data.domain.Pageable pageable) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public org.springframework.data.domain.Page<com.jordyma.blink.feed.domain.Feed> findFeedByQuery(long userId, @org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    org.springframework.data.domain.Pageable pageable) {
        return null;
    }
    
    @java.lang.Override()
    public long deleteKeywords(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.folder.Folder folder) {
        return 0L;
    }
    
    @java.lang.Override()
    public long deleteRecommend(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.folder.Folder folder) {
        return 0L;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.util.List<com.jordyma.blink.feed.domain.Feed> getFeedBetween(@org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime startDate, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime endDate, long userId) {
        return null;
    }
}