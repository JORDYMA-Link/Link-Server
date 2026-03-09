package com.jordyma.blink.featureflag

/**
 * Feature Flag 서비스
 * - AWS AppConfig 기반 런타임 전략 전환
 * - V1(Worker+SQS) ↔ V2(API Direct Coroutine)
 */
interface FeatureFlagService {
    /**
     * 현재 활성화된 요약 전략 반환
     * @return SummarizationStrategy.V1 or V2
     */
    fun getSummarizationStrategy(): SummarizationStrategy
    
    /**
     * 특정 유저에 대한 전략 반환 (A/B 테스트용)
     * @param userId 유저 ID
     * @return 유저별 전략
     */
    fun getSummarizationStrategy(userId: Long): SummarizationStrategy
    
    /**
     * 현재 flag 값을 강제로 새로고침
     */
    fun refresh()
}

/**
 * 요약 처리 전략
 */
enum class SummarizationStrategy {
    /**
     * V1: Worker + SQS 비동기 처리
     * - 안정성 우선
     * - 인프라 비용 높음
     */
    V1_WORKER_SQS,
    
    /**
     * V2: API 서버에서 코루틴으로 직접 처리
     * - 처리 속도 빠름
     * - 리소스 효율 높음
     */
    V2_API_DIRECT
}
