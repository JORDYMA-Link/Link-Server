package com.jordyma.blink.feed.strategy

import com.jordyma.blink.featureflag.SummarizationStrategy
import org.springframework.stereotype.Component

/**
 * 전략 선택 팩토리
 * - Feature Flag에 따라 적절한 Executor 반환
 */
@Component
class SummarizationExecutorFactory(
    private val v1Executor: V1SqsSummarizationExecutor,
    private val v2Executor: V2DirectSummarizationExecutor
) {
    /**
     * 전략에 맞는 Executor 반환
     */
    fun getExecutor(strategy: SummarizationStrategy): SummarizationExecutor {
        return when (strategy) {
            SummarizationStrategy.V1_WORKER_SQS -> v1Executor
            SummarizationStrategy.V2_API_DIRECT -> v2Executor
        }
    }
}
