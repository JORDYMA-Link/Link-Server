package com.jordyma.blink.infra.ratelimit

import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RateLimit

@Aspect
@Component
class SummarizeLimitAspect(
    private val limiter: SummarizeLimiter
) {
    @Around("@annotation(rateLimit)")
    fun checkRateLimit(joinPoint: ProceedingJoinPoint, rateLimit: RateLimit): Any? {
        val key = keyName

        if (!limiter.isAllowed(key)) {
            throw ApplicationException(ErrorCode.TOO_MANY_REQUEST, "잠시 후 다시 요청해주세요.")
        }

        return joinPoint.proceed()
    }

    companion object {
        const val keyName = "rate_limit"
    }
}