package com.jordyma.blink.domain.stats.service;

@org.springframework.stereotype.Service()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0004\b\u0017\u0018\u0000 \t2\u00020\u0001:\u0001\tB\u0011\u0012\n\b\u0001\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0005\u001a\u00020\u0006H\u0016J\b\u0010\u0007\u001a\u00020\u0006H\u0016J\b\u0010\b\u001a\u00020\u0006H\u0016R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0092\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/jordyma/blink/domain/stats/service/RedisLinkStatsProcessServiceImpl;", "Lcom/jordyma/blink/stats/service/LinkStatsProcessService;", "redisClient", "Lcom/jordyma/blink/redis/client/RedisClient;", "(Lcom/jordyma/blink/redis/client/RedisClient;)V", "getYesterdayDailyActiveUsers", "", "getYesterdayLinkViewCount", "saveLinkViewCount", "Companion", "blink-batch"})
public class RedisLinkStatsProcessServiceImpl implements com.jordyma.blink.stats.service.LinkStatsProcessService {
    @org.jetbrains.annotations.Nullable()
    private final com.jordyma.blink.redis.client.RedisClient redisClient = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LINK_VIEW_COUNT_KEY_PREFIX = "stats:link:view:count:";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String DAILY_ACTIVE_USERS_KEY_PREFIX = "stats:user:active:";
    @org.jetbrains.annotations.NotNull()
    public static final com.jordyma.blink.domain.stats.service.RedisLinkStatsProcessServiceImpl.Companion Companion = null;
    
    public RedisLinkStatsProcessServiceImpl(@org.springframework.beans.factory.annotation.Autowired(required = false)
    @org.jetbrains.annotations.Nullable()
    com.jordyma.blink.redis.client.RedisClient redisClient) {
        super();
    }
    
    @java.lang.Override()
    public long saveLinkViewCount() {
        return 0L;
    }
    
    @java.lang.Override()
    public long getYesterdayLinkViewCount() {
        return 0L;
    }
    
    @java.lang.Override()
    public long getYesterdayDailyActiveUsers() {
        return 0L;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/jordyma/blink/domain/stats/service/RedisLinkStatsProcessServiceImpl$Companion;", "", "()V", "DAILY_ACTIVE_USERS_KEY_PREFIX", "", "LINK_VIEW_COUNT_KEY_PREFIX", "blink-batch"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}