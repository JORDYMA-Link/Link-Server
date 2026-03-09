package com.jordyma.blink.scheduler;

@org.springframework.stereotype.Component()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0017\u0018\u0000 \u00102\u00020\u0001:\u0001\u0010B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u0007\u001a\u00020\bH\u0017J \u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0092\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/jordyma/blink/scheduler/StatsUpsertScheduler;", "", "statsStore", "Lcom/jordyma/blink/infra/stats/StatsStore;", "jdbcTemplate", "Lorg/springframework/jdbc/core/JdbcTemplate;", "(Lcom/jordyma/blink/infra/stats/StatsStore;Lorg/springframework/jdbc/core/JdbcTemplate;)V", "upsertHourlyStats", "", "upsertUserStatistic", "type", "", "count", "", "dateTime", "Ljava/time/LocalDateTime;", "Companion", "blink-batch"})
public class StatsUpsertScheduler {
    @org.jetbrains.annotations.NotNull()
    private final com.jordyma.blink.infra.stats.StatsStore statsStore = null;
    @org.jetbrains.annotations.NotNull()
    private final org.springframework.jdbc.core.JdbcTemplate jdbcTemplate = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTIVE_USER = "ACTIVE_USER";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LINK_VIEW = "LINK_VIEW";
    @org.jetbrains.annotations.NotNull()
    public static final com.jordyma.blink.scheduler.StatsUpsertScheduler.Companion Companion = null;
    
    public StatsUpsertScheduler(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.infra.stats.StatsStore statsStore, @org.jetbrains.annotations.NotNull()
    org.springframework.jdbc.core.JdbcTemplate jdbcTemplate) {
        super();
    }
    
    @org.springframework.scheduling.annotation.Scheduled(cron = "0 */30 * * * *")
    public void upsertHourlyStats() {
    }
    
    public void upsertUserStatistic(@org.jetbrains.annotations.NotNull()
    java.lang.String type, long count, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime dateTime) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/jordyma/blink/scheduler/StatsUpsertScheduler$Companion;", "", "()V", "ACTIVE_USER", "", "LINK_VIEW", "blink-batch"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}