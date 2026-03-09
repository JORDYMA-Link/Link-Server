package com.jordyma.blink.feed.strategy

import com.jordyma.blink.feed.domain.FeedRepository
import com.jordyma.blink.feed.domain.Status
import com.jordyma.blink.feed.domain.model.FeedSummarizeMessage
import com.jordyma.blink.feed_summarize_requester.sender.FeedSummarizeMessageSender
import com.jordyma.blink.feed_summarize_requester.sender.dto.FeedSummarizeMessage as ApiMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.stereotype.Component

/**
 * V1 전략: SQS 기반 비동기 처리
 * - Worker + SQS 인프라 활용
 * - 안정성 우선
 */
@Component
class V1SqsSummarizationExecutor(
    private val messageSender: FeedSummarizeMessageSender,
    private val feedRepository: FeedRepository,
    private val metrics: com.jordyma.blink.global.metrics.SummarizationMetrics
) : SummarizationExecutor {
    
    private val log = LoggerFactory.getLogger(this::class.java)
    
    override suspend fun execute(message: FeedSummarizeMessage): Long = coroutineScope {
        withContext(Dispatchers.IO) {
            val startTime = System.currentTimeMillis()
            try {
                MDC.put("feedId", message.feedId.toString())
                MDC.put("userId", message.userId.toString())
                MDC.put("strategy", "V1_WORKER_SQS")
                
                log.info("V1 Strategy: Sending to SQS, feedId=${message.feedId}")
                
                // FeedSummarizeMessage 타입 변환 (core → api)
                val apiMessage = ApiMessage(
                    link = message.link,
                    feedId = message.feedId,
                    userId = message.userId,
                    userName = message.userName,
                    originUrl = message.originUrl,
                    language = null // TODO: language 필드 추가 필요
                )
                
                messageSender.send(apiMessage)
                
                // Feed 상태를 REQUESTED로 업데이트
                val feed = feedRepository.findById(message.feedId)
                    .orElseThrow { IllegalArgumentException("Feed not found: ${message.feedId}") }
                feed.updateStatus(Status.REQUESTED)
                feedRepository.save(feed)
                
                val duration = System.currentTimeMillis() - startTime
                log.info("V1 Strategy completed: feedId=${message.feedId}, duration=${duration}ms")
                
                // 메트릭 기록
                metrics.recordStrategyUsed("V1_WORKER_SQS")
                metrics.recordDuration("V1_WORKER_SQS", duration)
                
                message.feedId
                
            } catch (e: Exception) {
                val duration = System.currentTimeMillis() - startTime
                log.error("V1 Strategy failed: feedId=${message.feedId}, duration=${duration}ms", e)
                
                // 메트릭 기록
                metrics.recordFailure("V1_WORKER_SQS", e.javaClass.simpleName)
                
                throw e
            } finally {
                MDC.clear()
            }
        }
    }
    
    override fun getStrategyName() = "V1_WORKER_SQS"
}
