package com.jordyma.blink.featureflag

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicReference

/**
 * AWS AppConfig 기반 Feature Flag 서비스
 * - 30초마다 AppConfig에서 설정 폴링
 * - 카나리 배포 지원 (userId 기반 모듈러 연산)
 */
@Service
class AppConfigFeatureFlagService(
    @Value("\${aws.appconfig.application:blink-service}") 
    private val application: String,
    
    @Value("\${aws.appconfig.environment:production}") 
    private val environment: String,
    
    @Value("\${aws.appconfig.configuration:summarization-strategy}") 
    private val configuration: String,
    
    @Value("\${aws.appconfig.poll-interval-seconds:30}") 
    private val pollInterval: Long,
    
    @Value("\${feature.summarization.default-strategy:V1_WORKER_SQS}")
    private val defaultStrategy: String,
    
    @Value("\${feature.summarization.user-ratio:0}")
    private val defaultUserRatio: Int
) : FeatureFlagService {
    
    private val log = LoggerFactory.getLogger(this::class.java)
    
    // AtomicReference로 thread-safe하게 관리
    private val currentStrategy = AtomicReference<SummarizationStrategy>(
        SummarizationStrategy.valueOf(defaultStrategy)
    )
    
    private val userStrategyRatio = AtomicReference(defaultUserRatio)
    
    @PostConstruct
    fun initialize() {
        log.info("FeatureFlagService initialized: strategy=${currentStrategy.get()}, ratio=${userStrategyRatio.get()}%")
        log.info("AppConfig settings: app=$application, env=$environment, config=$configuration")
    }
    
    /**
     * 전체 시스템의 기본 전략 반환
     */
    override fun getSummarizationStrategy(): SummarizationStrategy {
        return currentStrategy.get()
    }
    
    /**
     * 유저별 전략 반환 (카나리 배포용)
     * - 전체 전략이 V2라면 모든 유저가 V2 사용
     * - 전체 전략이 V1이라도 userStrategyRatio에 따라 일부 유저는 V2 사용
     */
    override fun getSummarizationStrategy(userId: Long): SummarizationStrategy {
        val strategy = currentStrategy.get()
        
        // 전체가 V2로 전환되었다면 모든 유저가 V2
        if (strategy == SummarizationStrategy.V2_API_DIRECT) {
            return SummarizationStrategy.V2_API_DIRECT
        }
        
        // 카나리 배포: userId 기반 모듈러 연산
        val ratio = userStrategyRatio.get()
        val userBucket = (userId % 100).toInt()
        
        return if (userBucket < ratio) {
            log.debug("User $userId → V2 (bucket=$userBucket, ratio=$ratio)")
            SummarizationStrategy.V2_API_DIRECT
        } else {
            log.debug("User $userId → V1 (bucket=$userBucket, ratio=$ratio)")
            SummarizationStrategy.V1_WORKER_SQS
        }
    }
    
    /**
     * AppConfig에서 설정 가져오기
     * TODO: AWS AppConfig SDK 연동 필요
     * 현재는 환경변수 기반으로 동작
     */
    @Scheduled(fixedDelayString = "\${aws.appconfig.poll-interval-seconds:30}000")
    override fun refresh() {
        try {
            // TODO: AWS AppConfig GetConfiguration API 호출
            // val config = appConfigClient.getConfiguration(...)
            
            // 현재는 환경변수 기반 (추후 AppConfig로 교체)
            log.debug("Polling AppConfig: strategy=${currentStrategy.get()}, ratio=${userStrategyRatio.get()}%")
            
        } catch (e: Exception) {
            log.error("Failed to refresh feature flags from AppConfig", e)
        }
    }
    
    /**
     * 테스트/디버그용: 전략 강제 설정
     */
    fun setStrategy(strategy: SummarizationStrategy, ratio: Int = 0) {
        currentStrategy.set(strategy)
        userStrategyRatio.set(ratio)
        log.info("Strategy updated manually: strategy=$strategy, ratio=$ratio%")
    }
}
