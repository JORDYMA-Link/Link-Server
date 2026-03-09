package com.jordyma.blink.config;

@org.springframework.context.annotation.Configuration()
@org.springframework.batch.core.configuration.annotation.EnableBatchProcessing()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0017\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0017\u00a8\u0006\u0007"}, d2 = {"Lcom/jordyma/blink/config/BatchConfig;", "", "()V", "batchTransactionManager", "Lorg/springframework/transaction/PlatformTransactionManager;", "dataSource", "Ljavax/sql/DataSource;", "blink-batch"})
public class BatchConfig {
    
    public BatchConfig() {
        super();
    }
    
    @org.springframework.context.annotation.Bean(name = {"transactionManager", "batchTransactionManager"})
    @org.jetbrains.annotations.NotNull()
    public org.springframework.transaction.PlatformTransactionManager batchTransactionManager(@org.jetbrains.annotations.NotNull()
    javax.sql.DataSource dataSource) {
        return null;
    }
}