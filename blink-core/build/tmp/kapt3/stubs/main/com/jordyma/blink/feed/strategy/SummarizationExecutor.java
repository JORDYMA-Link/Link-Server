package com.jordyma.blink.feed.strategy;

/**
 * 요약 실행 전략 인터페이스
 * - V1: SQS 기반 비동기 처리
 * - V2: API 서버 직접 코루틴 처리
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\bf\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u0007\u001a\u00020\bH&\u00a8\u0006\t"}, d2 = {"Lcom/jordyma/blink/feed/strategy/SummarizationExecutor;", "", "execute", "", "message", "Lcom/jordyma/blink/feed/domain/model/FeedSummarizeMessage;", "(Lcom/jordyma/blink/feed/domain/model/FeedSummarizeMessage;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getStrategyName", "", "blink-core"})
public abstract interface SummarizationExecutor {
    
    /**
     * 요약 실행 (순수 코루틴 기반)
     * @param message 요약 요청 메시지
     * @return feedId 반환
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object execute(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.feed.domain.model.FeedSummarizeMessage message, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    /**
     * 전략 이름
     * @return "V1_WORKER_SQS" or "V2_API_DIRECT"
     */
    @org.jetbrains.annotations.NotNull()
    public abstract java.lang.String getStrategyName();
}