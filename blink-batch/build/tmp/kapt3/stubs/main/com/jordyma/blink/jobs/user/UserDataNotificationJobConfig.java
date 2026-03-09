package com.jordyma.blink.jobs.user;

@org.springframework.context.annotation.Configuration()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0017\u0018\u0000 \u00142\u00020\u0001:\u0001\u0014B\'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\b\u0010\u000b\u001a\u00020\fH\u0017J\b\u0010\r\u001a\u00020\u000eH\u0017J\u000e\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010H\u0017J\u000e\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00110\u0013H\u0017R\u000e\u0010\u0004\u001a\u00020\u0005X\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0092\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/jordyma/blink/jobs/user/UserDataNotificationJobConfig;", "", "jobRepository", "Lorg/springframework/batch/core/repository/JobRepository;", "batchTransactionManager", "Lorg/springframework/transaction/PlatformTransactionManager;", "dataSource", "Ljavax/sql/DataSource;", "sendMessageService", "Lcom/jordyma/blink/domain/notification/service/SendUserDataNotificationService;", "(Lorg/springframework/batch/core/repository/JobRepository;Lorg/springframework/transaction/PlatformTransactionManager;Ljavax/sql/DataSource;Lcom/jordyma/blink/domain/notification/service/SendUserDataNotificationService;)V", "userDataNotificationJob", "Lorg/springframework/batch/core/Job;", "userDataNotificationJobStep", "Lorg/springframework/batch/core/Step;", "userDataNotificationReader", "Lorg/springframework/batch/item/database/JdbcCursorItemReader;", "Lcom/jordyma/blink/domain/notification/dto/UserDataNotificationDto;", "userDataNotificationWriter", "Lorg/springframework/batch/item/ItemWriter;", "Companion", "blink-batch"})
public class UserDataNotificationJobConfig {
    @org.jetbrains.annotations.NotNull()
    private final org.springframework.batch.core.repository.JobRepository jobRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final org.springframework.transaction.PlatformTransactionManager batchTransactionManager = null;
    @org.jetbrains.annotations.NotNull()
    private final javax.sql.DataSource dataSource = null;
    @org.jetbrains.annotations.NotNull()
    private final com.jordyma.blink.domain.notification.service.SendUserDataNotificationService sendMessageService = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String JOB_NAME = "UserDataNotificationJobConfig";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String STEP_NAME = "UserDataNotificationJobStep";
    @org.jetbrains.annotations.NotNull()
    public static final com.jordyma.blink.jobs.user.UserDataNotificationJobConfig.Companion Companion = null;
    
    public UserDataNotificationJobConfig(@org.jetbrains.annotations.NotNull()
    org.springframework.batch.core.repository.JobRepository jobRepository, @org.springframework.beans.factory.annotation.Qualifier(value = "batchTransactionManager")
    @org.jetbrains.annotations.NotNull()
    org.springframework.transaction.PlatformTransactionManager batchTransactionManager, @org.jetbrains.annotations.NotNull()
    javax.sql.DataSource dataSource, @org.jetbrains.annotations.NotNull()
    com.jordyma.blink.domain.notification.service.SendUserDataNotificationService sendMessageService) {
        super();
    }
    
    @org.springframework.context.annotation.Bean()
    @org.jetbrains.annotations.NotNull()
    public org.springframework.batch.core.Job userDataNotificationJob() {
        return null;
    }
    
    @org.springframework.context.annotation.Bean()
    @org.jetbrains.annotations.NotNull()
    public org.springframework.batch.core.Step userDataNotificationJobStep() {
        return null;
    }
    
    @org.springframework.context.annotation.Bean()
    @org.springframework.batch.core.configuration.annotation.StepScope()
    @org.jetbrains.annotations.NotNull()
    public org.springframework.batch.item.database.JdbcCursorItemReader<com.jordyma.blink.domain.notification.dto.UserDataNotificationDto> userDataNotificationReader() {
        return null;
    }
    
    @org.springframework.context.annotation.Bean()
    @org.jetbrains.annotations.NotNull()
    public org.springframework.batch.item.ItemWriter<com.jordyma.blink.domain.notification.dto.UserDataNotificationDto> userDataNotificationWriter() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/jordyma/blink/jobs/user/UserDataNotificationJobConfig$Companion;", "", "()V", "JOB_NAME", "", "STEP_NAME", "blink-batch"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}