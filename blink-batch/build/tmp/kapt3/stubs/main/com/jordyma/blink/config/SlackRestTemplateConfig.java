package com.jordyma.blink.config;

@org.springframework.context.annotation.Configuration()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0017\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0017\u00a8\u0006\u0005"}, d2 = {"Lcom/jordyma/blink/config/SlackRestTemplateConfig;", "", "()V", "slackRestTemplate", "Lorg/springframework/web/client/RestTemplate;", "blink-batch"})
public class SlackRestTemplateConfig {
    
    public SlackRestTemplateConfig() {
        super();
    }
    
    @org.springframework.context.annotation.Bean(name = {"slackRestTemplate"})
    @org.jetbrains.annotations.NotNull()
    public org.springframework.web.client.RestTemplate slackRestTemplate() {
        return null;
    }
}