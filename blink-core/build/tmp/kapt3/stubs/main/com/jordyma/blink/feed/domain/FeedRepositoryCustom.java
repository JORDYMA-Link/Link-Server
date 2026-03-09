package com.jordyma.blink.feed.domain;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0007\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J-\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010\u0004\u001a\u00020\u00052\b\u0010\u000b\u001a\u0004\u0018\u00010\f2\u0006\u0010\r\u001a\u00020\u0003H&\u00a2\u0006\u0002\u0010\u000eJ\u001e\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\n0\u00102\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0012\u001a\u00020\u0013H&J\u001e\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\n0\u00102\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0012\u001a\u00020\u0013H&J&\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\n0\u00102\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0012\u001a\u00020\u0013H&J\u001a\u0010\u0018\u001a\u0004\u0018\u00010\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u0003H&J&\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001e0\t2\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020 H&J&\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001e0\t2\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020 H&J\u001e\u0010\"\u001a\b\u0012\u0004\u0012\u00020\n0\u00102\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0012\u001a\u00020\u0013H&J&\u0010#\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010$\u001a\u00020 2\u0006\u0010%\u001a\u00020 2\u0006\u0010\u0011\u001a\u00020\u0003H&J\u0016\u0010&\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010\u001a\u001a\u00020\u001bH&\u00a8\u0006\'"}, d2 = {"Lcom/jordyma/blink/feed/domain/FeedRepositoryCustom;", "", "deleteAllByFolder", "", "folder", "Lcom/jordyma/blink/folder/Folder;", "deleteKeywords", "deleteRecommend", "findAllByFolder", "", "Lcom/jordyma/blink/feed/domain/Feed;", "cursor", "", "pageSize", "(Lcom/jordyma/blink/folder/Folder;Ljava/lang/Integer;J)Ljava/util/List;", "findAllByUser", "Lorg/springframework/data/domain/Page;", "userId", "pageable", "Lorg/springframework/data/domain/Pageable;", "findBookmarkedFeeds", "findFeedByQuery", "query", "", "findFeedDetail", "Lcom/jordyma/blink/feed/vo/FeedDetailVo;", "user", "Lcom/jordyma/blink/user/User;", "feedId", "findFeedFolderDtoByUserIdAndBetweenDate", "Lcom/jordyma/blink/feed/vo/FeedFolderVo;", "startOfMonth", "Ljava/time/LocalDateTime;", "endOfMonth", "findUnclassifiedFeeds", "getFeedBetween", "startDate", "endDate", "getProcessing", "blink-core"})
public abstract interface FeedRepositoryCustom {
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.util.List<com.jordyma.blink.feed.vo.FeedFolderVo> findFeedFolderDtoByUserIdAndBetweenDate(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.user.User user, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime startOfMonth, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime endOfMonth);
    
    @org.jetbrains.annotations.Nullable()
    public abstract com.jordyma.blink.feed.vo.FeedDetailVo findFeedDetail(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.user.User user, long feedId);
    
    public abstract long deleteAllByFolder(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.folder.Folder folder);
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.util.List<com.jordyma.blink.feed.domain.Feed> getProcessing(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.user.User user);
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.util.List<com.jordyma.blink.feed.domain.Feed> findAllByFolder(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.folder.Folder folder, @org.jetbrains.annotations.Nullable()
    java.lang.Integer cursor, long pageSize);
    
    @org.jetbrains.annotations.NotNull()
    public abstract org.springframework.data.domain.Page<com.jordyma.blink.feed.domain.Feed> findAllByUser(long userId, @org.jetbrains.annotations.NotNull()
    org.springframework.data.domain.Pageable pageable);
    
    @org.jetbrains.annotations.NotNull()
    public abstract org.springframework.data.domain.Page<com.jordyma.blink.feed.domain.Feed> findUnclassifiedFeeds(long userId, @org.jetbrains.annotations.NotNull()
    org.springframework.data.domain.Pageable pageable);
    
    @org.jetbrains.annotations.NotNull()
    public abstract org.springframework.data.domain.Page<com.jordyma.blink.feed.domain.Feed> findBookmarkedFeeds(long userId, @org.jetbrains.annotations.NotNull()
    org.springframework.data.domain.Pageable pageable);
    
    @org.jetbrains.annotations.NotNull()
    public abstract org.springframework.data.domain.Page<com.jordyma.blink.feed.domain.Feed> findFeedByQuery(long userId, @org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    org.springframework.data.domain.Pageable pageable);
    
    public abstract long deleteKeywords(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.folder.Folder folder);
    
    public abstract long deleteRecommend(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.folder.Folder folder);
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.util.List<com.jordyma.blink.feed.vo.FeedFolderVo> findFeedFolderDtoByUserIdAndBetweenDate(long userId, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime startOfMonth, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime endOfMonth);
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.util.List<com.jordyma.blink.feed.domain.Feed> getFeedBetween(@org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime startDate, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime endDate, long userId);
}