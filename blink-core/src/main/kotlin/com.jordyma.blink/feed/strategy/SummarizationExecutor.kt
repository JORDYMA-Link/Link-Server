package com.jordyma.blink.feed.strategy

import com.jordyma.blink.feed.domain.model.FeedSummarizeMessage

/**
 * 요약 실행 전략 인터페이스
 * - V1: SQS 기반 비동기 처리
 * - V2: API 서버 직접 코루틴 처리
 */
interface SummarizationExecutor {
    /**
     * 요약 실행 (순수 코루틴 기반)
     * @param message 요약 요청 메시지
     * @return feedId 반환
     */
    suspend fun execute(message: FeedSummarizeMessage): Long
    
    /**
     * 전략 이름
     * @return "V1_WORKER_SQS" or "V2_API_DIRECT"
     */
    fun getStrategyName(): String
}
