package com.jordyma.blink.scheduler;

@org.springframework.context.annotation.Configuration()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b\u0017\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u0007\u001a\u00020\bH\u0017R\u000e\u0010\u0002\u001a\u00020\u0003X\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0092\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/jordyma/blink/scheduler/UserDataNotificationJobScheduler;", "", "jobLauncher", "Lorg/springframework/batch/core/launch/JobLauncher;", "userDataNotificationJobConfig", "Lcom/jordyma/blink/jobs/user/UserDataNotificationJobConfig;", "(Lorg/springframework/batch/core/launch/JobLauncher;Lcom/jordyma/blink/jobs/user/UserDataNotificationJobConfig;)V", "doUserDataNotificationScheduler", "", "blink-batch"})
public class UserDataNotificationJobScheduler {
    @org.jetbrains.annotations.NotNull()
    private final org.springframework.batch.core.launch.JobLauncher jobLauncher = null;
    @org.jetbrains.annotations.NotNull()
    private final com.jordyma.blink.jobs.user.UserDataNotificationJobConfig userDataNotificationJobConfig = null;
    
    public UserDataNotificationJobScheduler(@org.jetbrains.annotations.NotNull()
    org.springframework.batch.core.launch.JobLauncher jobLauncher, @org.jetbrains.annotations.NotNull()
    com.jordyma.blink.jobs.user.UserDataNotificationJobConfig userDataNotificationJobConfig) {
        super();
    }
    
    @org.springframework.scheduling.annotation.Scheduled(cron = "${scheduler.user-notification.cron}")
    public void doUserDataNotificationScheduler() {
    }
}