package com.jordyma.blink.redis.client;

@org.springframework.stereotype.Component()
@org.springframework.boot.autoconfigure.condition.ConditionalOnProperty(name = {"spring.data.redis.enabled"}, havingValue = "true", matchIfMissing = false)
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\b\u0017\u0018\u00002\u00020\u0001B\u0019\u0012\u0012\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0005J\u0017\u0010\u0006\u001a\u0004\u0018\u00010\u00072\u0006\u0010\b\u001a\u00020\u0004H\u0016\u00a2\u0006\u0002\u0010\tJ.\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\f\u001a\u00020\u00042\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00040\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00040\u000eH\u0016J\u0018\u0010\u0010\u001a\u00020\u00112\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u0007H\u0016J\u0010\u0010\u0013\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u0004H\u0016J\u0017\u0010\u0014\u001a\u0004\u0018\u00010\u00072\u0006\u0010\b\u001a\u00020\u0004H\u0016\u00a2\u0006\u0002\u0010\tJ\u001f\u0010\u0015\u001a\u0004\u0018\u00010\u00072\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u0004H\u0016\u00a2\u0006\u0002\u0010\u0017J\u0017\u0010\u0018\u001a\u0004\u0018\u00010\u00072\u0006\u0010\b\u001a\u00020\u0004H\u0016\u00a2\u0006\u0002\u0010\tJ\u0018\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u0004H\u0016J \u0010\u001b\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u0004H\u0016J \u0010\u001f\u001a\u00020 2\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010!\u001a\u00020\u00072\u0006\u0010\"\u001a\u00020\u0007H\u0016J \u0010#\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010!\u001a\u00020\u001d2\u0006\u0010\"\u001a\u00020\u001dH\u0016R\u001a\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u0003X\u0092\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006$"}, d2 = {"Lcom/jordyma/blink/redis/client/RedisClientImpl;", "Lcom/jordyma/blink/redis/client/RedisClient;", "redisTemplate", "Lorg/springframework/data/redis/core/RedisTemplate;", "", "(Lorg/springframework/data/redis/core/RedisTemplate;)V", "decr", "", "key", "(Ljava/lang/String;)Ljava/lang/Long;", "eval", "", "script", "keys", "", "args", "expire", "", "seconds", "get", "incr", "sadd", "value", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Long;", "scard", "set", "", "zadd", "score", "", "member", "zcount", "", "min", "max", "zremrangebyscore", "blink-core"})
public class RedisClientImpl implements com.jordyma.blink.redis.client.RedisClient {
    @org.jetbrains.annotations.NotNull()
    private final org.springframework.data.redis.core.RedisTemplate<java.lang.String, java.lang.String> redisTemplate = null;
    
    public RedisClientImpl(@org.jetbrains.annotations.NotNull()
    org.springframework.data.redis.core.RedisTemplate<java.lang.String, java.lang.String> redisTemplate) {
        super();
    }
    
    @java.lang.Override()
    public void set(@org.jetbrains.annotations.NotNull()
    java.lang.String key, @org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String get(@org.jetbrains.annotations.NotNull()
    java.lang.String key) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Long decr(@org.jetbrains.annotations.NotNull()
    java.lang.String key) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Long incr(@org.jetbrains.annotations.NotNull()
    java.lang.String key) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Long sadd(@org.jetbrains.annotations.NotNull()
    java.lang.String key, @org.jetbrains.annotations.NotNull()
    java.lang.String value) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Long scard(@org.jetbrains.annotations.NotNull()
    java.lang.String key) {
        return null;
    }
    
    @java.lang.Override()
    public boolean expire(@org.jetbrains.annotations.NotNull()
    java.lang.String key, long seconds) {
        return false;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object eval(@org.jetbrains.annotations.NotNull()
    java.lang.String script, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> keys, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> args) {
        return null;
    }
    
    @java.lang.Override()
    public int zcount(@org.jetbrains.annotations.NotNull()
    java.lang.String key, long min, long max) {
        return 0;
    }
    
    @java.lang.Override()
    public long zadd(@org.jetbrains.annotations.NotNull()
    java.lang.String key, double score, @org.jetbrains.annotations.NotNull()
    java.lang.String member) {
        return 0L;
    }
    
    @java.lang.Override()
    public long zremrangebyscore(@org.jetbrains.annotations.NotNull()
    java.lang.String key, double min, double max) {
        return 0L;
    }
}