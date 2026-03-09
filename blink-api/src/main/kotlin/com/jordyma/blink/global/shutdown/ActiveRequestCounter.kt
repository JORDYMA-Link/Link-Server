package com.jordyma.blink.global.shutdown

import jakarta.annotation.PreDestroy
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicInteger

/**
 * V2 Executor의 활성 요청 카운터
 * - Graceful Shutdown 시 실행 중인 요청 추적
 * - PreDestroy에서 완료 대기
 */
@Component
class ActiveRequestCounter {
    private val activeRequests = AtomicInteger(0)
    private val log = LoggerFactory.getLogger(this::class.java)
    
    fun increment() {
        val count = activeRequests.incrementAndGet()
        log.debug("Active requests incremented: $count")
    }
    
    fun decrement() {
        val count = activeRequests.decrementAndGet()
        log.debug("Active requests decremented: $count")
    }
    
    fun getCount(): Int = activeRequests.get()
    
    @PreDestroy
    fun waitForCompletion() {
        val count = activeRequests.get()
        if (count == 0) {
            log.info("No active requests. Shutting down immediately.")
            return
        }
        
        log.info("Waiting for $count active requests to complete...")
        
        val maxWaitSeconds = 30
        val startTime = System.currentTimeMillis()
        
        while (activeRequests.get() > 0) {
            val elapsed = (System.currentTimeMillis() - startTime) / 1000
            if (elapsed > maxWaitSeconds) {
                val remaining = activeRequests.get()
                log.warn("Timeout after ${maxWaitSeconds}s: $remaining requests still active. Forcing shutdown.")
                break
            }
            
            Thread.sleep(1000)
            log.info("Waiting... ${activeRequests.get()} requests remaining (${elapsed}s elapsed)")
        }
        
        log.info("All requests completed. Shutting down gracefully.")
    }
}
