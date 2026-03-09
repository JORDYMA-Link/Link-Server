package com.jordyma.blink;

@org.springframework.boot.autoconfigure.SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class})
@org.springframework.batch.core.configuration.annotation.EnableBatchProcessing()
@org.springframework.scheduling.annotation.EnableScheduling()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0017\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/jordyma/blink/BlinkBatchApplication;", "", "()V", "blink-batch"})
public class BlinkBatchApplication {
    
    public BlinkBatchApplication() {
        super();
    }
}