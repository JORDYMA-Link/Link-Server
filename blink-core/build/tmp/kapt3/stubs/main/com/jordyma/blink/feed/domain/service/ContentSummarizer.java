package com.jordyma.blink.feed.domain.service;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J<\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\fH&\u00a8\u0006\r"}, d2 = {"Lcom/jordyma/blink/feed/domain/service/ContentSummarizer;", "", "summarize", "Lcom/jordyma/blink/feed/domain/service/PromptResponse;", "content", "", "link", "folders", "userId", "", "feedId", "language", "Lcom/jordyma/blink/user/LanguageType;", "blink-core"})
public abstract interface ContentSummarizer {
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.jordyma.blink.feed.domain.service.PromptResponse summarize(@org.jetbrains.annotations.NotNull()
    java.lang.String content, @org.jetbrains.annotations.NotNull()
    java.lang.String link, @org.jetbrains.annotations.NotNull()
    java.lang.String folders, long userId, long feedId, @org.jetbrains.annotations.Nullable()
    com.jordyma.blink.user.LanguageType language);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}