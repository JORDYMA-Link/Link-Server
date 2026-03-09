package com.jordyma.blink.feed.usecase;

@org.springframework.stereotype.Service()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0017\u0018\u00002\u00020\u0001BE\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u00a2\u0006\u0002\u0010\u0012J\u0012\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u0015\u001a\u00020\u0016H\u0012J\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aH\u0012J\u0010\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eH\u0017R\u000e\u0010\b\u001a\u00020\tX\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0092\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/jordyma/blink/feed/usecase/SummarizeFeedUsecase;", "", "feedRepository", "Lcom/jordyma/blink/feed/domain/FeedRepository;", "userRepository", "Lcom/jordyma/blink/user/UserRepository;", "htmlParser", "Lcom/jordyma/blink/feed/domain/service/PageParser;", "contentSummarizer", "Lcom/jordyma/blink/feed/domain/service/ContentSummarizer;", "folderService", "Lcom/jordyma/blink/folder/domain/service/FolderService;", "feedSummarizeService", "Lcom/jordyma/blink/feed/domain/service/FeedSummarizeService;", "sendNotificationService", "Lcom/jordyma/blink/notification/service/SendNotificationService;", "logger", "Lcom/jordyma/blink/common/logger/BlinkLogger;", "(Lcom/jordyma/blink/feed/domain/FeedRepository;Lcom/jordyma/blink/user/UserRepository;Lcom/jordyma/blink/feed/domain/service/PageParser;Lcom/jordyma/blink/feed/domain/service/ContentSummarizer;Lcom/jordyma/blink/folder/domain/service/FolderService;Lcom/jordyma/blink/feed/domain/service/FeedSummarizeService;Lcom/jordyma/blink/notification/service/SendNotificationService;Lcom/jordyma/blink/common/logger/BlinkLogger;)V", "findBrunch", "Lcom/jordyma/blink/feed/domain/Source;", "link", "", "findFeedOrElseThrow", "Lcom/jordyma/blink/feed/domain/Feed;", "feedId", "", "summarizeFeed", "", "input", "Lcom/jordyma/blink/feed/domain/model/FeedSummarizeMessage;", "blink-core"})
public class SummarizeFeedUsecase {
    @org.jetbrains.annotations.NotNull()
    private final com.jordyma.blink.feed.domain.FeedRepository feedRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.jordyma.blink.user.UserRepository userRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.jordyma.blink.feed.domain.service.PageParser htmlParser = null;
    @org.jetbrains.annotations.NotNull()
    private final com.jordyma.blink.feed.domain.service.ContentSummarizer contentSummarizer = null;
    @org.jetbrains.annotations.NotNull()
    private final com.jordyma.blink.folder.domain.service.FolderService folderService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.jordyma.blink.feed.domain.service.FeedSummarizeService feedSummarizeService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.jordyma.blink.notification.service.SendNotificationService sendNotificationService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.jordyma.blink.common.logger.BlinkLogger logger = null;
    
    public SummarizeFeedUsecase(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.feed.domain.FeedRepository feedRepository, @org.jetbrains.annotations.NotNull()
    com.jordyma.blink.user.UserRepository userRepository, @org.jetbrains.annotations.NotNull()
    com.jordyma.blink.feed.domain.service.PageParser htmlParser, @org.jetbrains.annotations.NotNull()
    com.jordyma.blink.feed.domain.service.ContentSummarizer contentSummarizer, @org.jetbrains.annotations.NotNull()
    com.jordyma.blink.folder.domain.service.FolderService folderService, @org.jetbrains.annotations.NotNull()
    com.jordyma.blink.feed.domain.service.FeedSummarizeService feedSummarizeService, @org.jetbrains.annotations.NotNull()
    com.jordyma.blink.notification.service.SendNotificationService sendNotificationService, @org.jetbrains.annotations.NotNull()
    com.jordyma.blink.common.logger.BlinkLogger logger) {
        super();
    }
    
    @org.springframework.transaction.annotation.Transactional()
    public void summarizeFeed(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.feed.domain.model.FeedSummarizeMessage input) {
    }
    
    private com.jordyma.blink.feed.domain.Source findBrunch(java.lang.String link) {
        return null;
    }
    
    private com.jordyma.blink.feed.domain.Feed findFeedOrElseThrow(long feedId) {
        return null;
    }
}