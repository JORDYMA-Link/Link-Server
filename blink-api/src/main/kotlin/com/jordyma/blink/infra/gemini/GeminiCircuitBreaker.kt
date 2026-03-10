package com.jordyma.blink.infra.gemini

import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Instant

/**
 * Gemini API에 대한 경량 Circuit Breaker
 * - 60분 동안 429 응답이 90% 이상이면 Circuit OPEN
 * - Circuit OPEN 상태에서는 즉시 에러 반환
 */
@Component
class GeminiCircuitBreaker(
    private val redissonClient: RedissonClient
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    
    companion object {
        private const val CIRCUIT_STATE_KEY = "blink:circuit:gemini:state"
        private const val REQUEST_LOG_KEY = "blink:circuit:gemini:requests"
        private const val WINDOW_SECONDS = 3600L // 60분
        private const val FAILURE_THRESHOLD = 0.9 // 90%
        private const val MIN_REQUESTS = 10 // 최소 요청 수 (서킷 판단을 위해)
    }
    
    enum class CircuitState {
        CLOSED,  // 정상 동작
        OPEN     // 차단 상태
    }
    
    enum class RequestResult {
        SUCCESS,
        FAILURE_429
    }
    
    /**
     * Circuit 상태 확인
     */
    fun isOpen(): Boolean {
        val bucket = redissonClient.getBucket<String>(CIRCUIT_STATE_KEY)
        val state = bucket.get()
        return state == CircuitState.OPEN.name
    }
    
    /**
     * Circuit을 통과할 수 있는지 확인
     * @throws GeminiCircuitOpenException Circuit이 OPEN 상태인 경우
     */
    fun checkCircuit() {
        if (isOpen()) {
            log.warn("Circuit is OPEN - blocking Gemini API request")
            throw GeminiCircuitOpenException("Gemini API가 일시적으로 사용 불가능합니다. 잠시 후 다시 시도해주세요.")
        }
    }
    
    /**
     * 성공 기록
     */
    fun recordSuccess() {
        recordRequest(RequestResult.SUCCESS)
    }
    
    /**
     * 429 에러 기록
     */
    fun record429Failure() {
        recordRequest(RequestResult.FAILURE_429)
    }
    
    /**
     * 요청 결과 기록
     */
    private fun recordRequest(result: RequestResult) {
        try {
            val now = Instant.now().epochSecond
            val sortedSet = redissonClient.getScoredSortedSet<String>(REQUEST_LOG_KEY)
            
            // 현재 요청 기록 (score는 timestamp)
            sortedSet.add(now.toDouble(), "${now}:${result.name}")
            
            // 60분 이전 데이터 삭제
            val cutoffTime = now - WINDOW_SECONDS
            sortedSet.removeRangeByScore(Double.MIN_VALUE, true, cutoffTime.toDouble(), false)
            
            // Circuit 상태 평가
            evaluateCircuit(now)
        } catch (e: Exception) {
            log.error("Failed to record request", e)
        }
    }
    
    /**
     * Circuit 상태 평가 및 갱신
     */
    private fun evaluateCircuit(now: Long) {
        try {
            val sortedSet = redissonClient.getScoredSortedSet<String>(REQUEST_LOG_KEY)
            val cutoffTime = now - WINDOW_SECONDS
            
            // 60분 이내의 모든 요청 조회
            val recentRequests = sortedSet.valueRange(cutoffTime.toDouble(), true, Double.MAX_VALUE, true)
            
            if (recentRequests.size < MIN_REQUESTS) {
                // 최소 요청 수 미만이면 평가하지 않음
                return
            }
            
            val failure429Count = recentRequests.count { it.endsWith(RequestResult.FAILURE_429.name) }
            val totalRequests = recentRequests.size
            val failure429Rate = failure429Count.toDouble() / totalRequests
            
            val bucket = redissonClient.getBucket<String>(CIRCUIT_STATE_KEY)
            
            if (failure429Rate >= FAILURE_THRESHOLD) {
                // Circuit OPEN
                val ttlSeconds = WINDOW_SECONDS
                bucket.set(CircuitState.OPEN.name, ttlSeconds, java.util.concurrent.TimeUnit.SECONDS)
                log.error(
                    "Circuit OPENED - 429 rate: ${"%.2f".format(failure429Rate * 100)}% " +
                    "($failure429Count/$totalRequests in last 60min)"
                )
            } else {
                // Circuit CLOSED
                val currentState = bucket.get()
                if (currentState == CircuitState.OPEN.name) {
                    bucket.delete()
                    log.info(
                        "Circuit CLOSED - 429 rate improved: ${"%.2f".format(failure429Rate * 100)}% " +
                        "($failure429Count/$totalRequests)"
                    )
                }
            }
        } catch (e: Exception) {
            log.error("Failed to evaluate circuit", e)
        }
    }
    
    /**
     * Circuit 상태 정보 조회 (모니터링용)
     */
    fun getCircuitStatus(): CircuitStatus {
        return try {
            val bucket = redissonClient.getBucket<String>(CIRCUIT_STATE_KEY)
            val state = bucket.get() ?: CircuitState.CLOSED.name
            
            val now = Instant.now().epochSecond
            val cutoffTime = now - WINDOW_SECONDS
            val sortedSet = redissonClient.getScoredSortedSet<String>(REQUEST_LOG_KEY)
            
            val recentRequests = sortedSet.valueRange(cutoffTime.toDouble(), true, Double.MAX_VALUE, true)
            val successCount = recentRequests.count { it.endsWith(RequestResult.SUCCESS.name) }.toLong()
            val failure429Count = recentRequests.count { it.endsWith(RequestResult.FAILURE_429.name) }.toLong()
            val totalRequests = recentRequests.size.toLong()
            
            val failure429Rate = if (totalRequests > 0) {
                failure429Count.toDouble() / totalRequests
            } else {
                0.0
            }
            
            CircuitStatus(
                state = CircuitState.valueOf(state),
                successCount = successCount,
                failure429Count = failure429Count,
                failure429Rate = failure429Rate,
                totalRequests = totalRequests
            )
        } catch (e: Exception) {
            log.error("Failed to get circuit status", e)
            CircuitStatus(
                state = CircuitState.CLOSED,
                successCount = 0,
                failure429Count = 0,
                failure429Rate = 0.0,
                totalRequests = 0
            )
        }
    }
    
    /**
     * Circuit 강제 리셋 (관리자용)
     */
    fun reset() {
        try {
            redissonClient.getBucket<String>(CIRCUIT_STATE_KEY).delete()
            redissonClient.getScoredSortedSet<String>(REQUEST_LOG_KEY).delete()
            log.info("Circuit breaker reset successfully")
        } catch (e: Exception) {
            log.error("Failed to reset circuit breaker", e)
        }
    }
}

data class CircuitStatus(
    val state: GeminiCircuitBreaker.CircuitState,
    val successCount: Long,
    val failure429Count: Long,
    val failure429Rate: Double,
    val totalRequests: Long
)

class GeminiCircuitOpenException(message: String) : RuntimeException(message)
