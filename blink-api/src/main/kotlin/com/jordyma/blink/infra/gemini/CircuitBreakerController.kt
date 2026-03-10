package com.jordyma.blink.infra.gemini

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin/circuit-breaker")
class CircuitBreakerController(
    private val circuitBreaker: GeminiCircuitBreaker
) {
    
    /**
     * Circuit Breaker 상태 조회
     */
    @GetMapping("/status")
    fun getStatus(): CircuitStatusResponse {
        val status = circuitBreaker.getCircuitStatus()
        return CircuitStatusResponse(
            state = status.state.name,
            successCount = status.successCount,
            failure429Count = status.failure429Count,
            failure429Rate = String.format("%.2f%%", status.failure429Rate * 100),
            totalRequests = status.totalRequests,
            isOpen = circuitBreaker.isOpen()
        )
    }
    
    /**
     * Circuit Breaker 강제 리셋
     */
    @PostMapping("/reset")
    fun reset(): ResetResponse {
        circuitBreaker.reset()
        return ResetResponse(
            success = true,
            message = "Circuit breaker has been reset"
        )
    }
}

data class CircuitStatusResponse(
    val state: String,
    val successCount: Long,
    val failure429Count: Long,
    val failure429Rate: String,
    val totalRequests: Long,
    val isOpen: Boolean
)

data class ResetResponse(
    val success: Boolean,
    val message: String
)
