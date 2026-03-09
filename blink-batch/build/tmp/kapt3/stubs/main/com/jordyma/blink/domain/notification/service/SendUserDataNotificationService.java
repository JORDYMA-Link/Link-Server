package com.jordyma.blink.domain.notification.service;

@org.springframework.stereotype.Service()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0017\u0018\u00002\u00020\u0001B-\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0001\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016R\u000e\u0010\u0004\u001a\u00020\u0003X\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0092\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/jordyma/blink/domain/notification/service/SendUserDataNotificationService;", "", "channelId", "", "botToken", "restTemplate", "Lorg/springframework/web/client/RestTemplate;", "linkStatsProcessService", "Lcom/jordyma/blink/stats/service/LinkStatsProcessService;", "(Ljava/lang/String;Ljava/lang/String;Lorg/springframework/web/client/RestTemplate;Lcom/jordyma/blink/stats/service/LinkStatsProcessService;)V", "sendUserDataNotification", "", "data", "Lcom/jordyma/blink/domain/notification/dto/UserDataNotificationDto;", "blink-batch"})
public class SendUserDataNotificationService {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String channelId = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String botToken = null;
    @org.jetbrains.annotations.NotNull()
    private final org.springframework.web.client.RestTemplate restTemplate = null;
    @org.jetbrains.annotations.NotNull()
    private final com.jordyma.blink.stats.service.LinkStatsProcessService linkStatsProcessService = null;
    
    public SendUserDataNotificationService(@org.springframework.beans.factory.annotation.Value(value = "${slack.channel-id}")
    @org.jetbrains.annotations.NotNull()
    java.lang.String channelId, @org.springframework.beans.factory.annotation.Value(value = "${slack.token}")
    @org.jetbrains.annotations.NotNull()
    java.lang.String botToken, @org.springframework.beans.factory.annotation.Qualifier(value = "slackRestTemplate")
    @org.jetbrains.annotations.NotNull()
    org.springframework.web.client.RestTemplate restTemplate, @org.springframework.beans.factory.annotation.Qualifier(value = "linkStatsProcessServiceImpl")
    @org.jetbrains.annotations.NotNull()
    com.jordyma.blink.stats.service.LinkStatsProcessService linkStatsProcessService) {
        super();
    }
    
    public void sendUserDataNotification(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.domain.notification.dto.UserDataNotificationDto data) {
    }
}