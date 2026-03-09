package com.jordyma.blink.featureflag;

/**
 * Feature Flag 서비스
 * - AWS AppConfig 기반 런타임 전략 전환
 * - V1(Worker+SQS) ↔ V2(API Direct Coroutine)
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\b\u0010\u0006\u001a\u00020\u0007H&\u00a8\u0006\b"}, d2 = {"Lcom/jordyma/blink/featureflag/FeatureFlagService;", "", "getSummarizationStrategy", "Lcom/jordyma/blink/featureflag/SummarizationStrategy;", "userId", "", "refresh", "", "blink-core"})
public abstract interface FeatureFlagService {
    
    /**
     * 현재 활성화된 요약 전략 반환
     * @return SummarizationStrategy.V1 or V2
     */
    @org.jetbrains.annotations.NotNull()
    public abstract com.jordyma.blink.featureflag.SummarizationStrategy getSummarizationStrategy();
    
    /**
     * 특정 유저에 대한 전략 반환 (A/B 테스트용)
     * @param userId 유저 ID
     * @return 유저별 전략
     */
    @org.jetbrains.annotations.NotNull()
    public abstract com.jordyma.blink.featureflag.SummarizationStrategy getSummarizationStrategy(long userId);
    
    /**
     * 현재 flag 값을 강제로 새로고침
     */
    public abstract void refresh();
}