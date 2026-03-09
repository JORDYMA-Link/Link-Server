package com.jordyma.blink.feed.domain.service;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\u001e\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H&J0\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\bH&\u00a8\u0006\u0012"}, d2 = {"Lcom/jordyma/blink/feed/domain/service/FeedSummarizeService;", "", "createRecommendFolders", "", "feed", "Lcom/jordyma/blink/feed/domain/Feed;", "category", "", "", "updateSummarizedFeed", "content", "Lcom/jordyma/blink/feed/domain/service/PromptResponse;", "brunch", "Lcom/jordyma/blink/feed/domain/Source;", "feedId", "", "userId", "thumbnailImage", "blink-core"})
public abstract interface FeedSummarizeService {
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.jordyma.blink.feed.domain.Feed updateSummarizedFeed(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.feed.domain.service.PromptResponse content, @org.jetbrains.annotations.NotNull()
    com.jordyma.blink.feed.domain.Source brunch, long feedId, long userId, @org.jetbrains.annotations.NotNull()
    java.lang.String thumbnailImage);
    
    public abstract void createRecommendFolders(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.feed.domain.Feed feed, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> category);
}