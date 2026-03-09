package com.jordyma.blink.domain.stats.service;

@org.springframework.stereotype.Service()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\b\u0017\u0018\u00002\u00020\u0001B\u0017\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010\t\u001a\u00020\bH\u0016J\b\u0010\n\u001a\u00020\bH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0092\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/jordyma/blink/domain/stats/service/LinkStatsProcessServiceImpl;", "Lcom/jordyma/blink/stats/service/LinkStatsProcessService;", "linkStatsIncreaseService", "Lcom/jordyma/blink/stats/service/LinkStatsIncreaseService;", "statsStore", "Lcom/jordyma/blink/infra/stats/StatsStore;", "(Lcom/jordyma/blink/stats/service/LinkStatsIncreaseService;Lcom/jordyma/blink/infra/stats/StatsStore;)V", "getYesterdayDailyActiveUsers", "", "getYesterdayLinkViewCount", "saveLinkViewCount", "blink-batch"})
public class LinkStatsProcessServiceImpl implements com.jordyma.blink.stats.service.LinkStatsProcessService {
    @org.jetbrains.annotations.NotNull()
    private final com.jordyma.blink.stats.service.LinkStatsIncreaseService linkStatsIncreaseService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.jordyma.blink.infra.stats.StatsStore statsStore = null;
    
    public LinkStatsProcessServiceImpl(@org.springframework.beans.factory.annotation.Qualifier(value = "linkStatsIncreaseServiceImpl")
    @org.jetbrains.annotations.NotNull()
    com.jordyma.blink.stats.service.LinkStatsIncreaseService linkStatsIncreaseService, @org.jetbrains.annotations.NotNull()
    com.jordyma.blink.infra.stats.StatsStore statsStore) {
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
}