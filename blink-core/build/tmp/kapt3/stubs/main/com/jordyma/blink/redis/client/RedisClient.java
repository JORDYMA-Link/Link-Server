package com.jordyma.blink.redis.client;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J\u0017\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a2\u0006\u0002\u0010\u0006J.\u0010\u0007\u001a\u0004\u0018\u00010\u00012\u0006\u0010\b\u001a\u00020\u00052\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00050\nH&J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u0003H&J\u0012\u0010\u000f\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0017\u0010\u0010\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a2\u0006\u0002\u0010\u0006J\u001f\u0010\u0011\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u0005H&\u00a2\u0006\u0002\u0010\u0013J\u0017\u0010\u0014\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u0005H&J \u0010\u0017\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0005H&J \u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u001d\u001a\u00020\u00032\u0006\u0010\u001e\u001a\u00020\u0003H&J \u0010\u001f\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u001d\u001a\u00020\u00192\u0006\u0010\u001e\u001a\u00020\u0019H&\u00a8\u0006 "}, d2 = {"Lcom/jordyma/blink/redis/client/RedisClient;", "", "decr", "", "key", "", "(Ljava/lang/String;)Ljava/lang/Long;", "eval", "script", "keys", "", "args", "expire", "", "seconds", "get", "incr", "sadd", "value", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Long;", "scard", "set", "", "zadd", "score", "", "member", "zcount", "", "min", "max", "zremrangebyscore", "blink-core"})
public abstract interface RedisClient {
    
    public abstract void set(@org.jetbrains.annotations.NotNull()
    java.lang.String key, @org.jetbrains.annotations.NotNull()
    java.lang.String value);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.String get(@org.jetbrains.annotations.NotNull()
    java.lang.String key);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Long decr(@org.jetbrains.annotations.NotNull()
    java.lang.String key);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Long incr(@org.jetbrains.annotations.NotNull()
    java.lang.String key);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Long sadd(@org.jetbrains.annotations.NotNull()
    java.lang.String key, @org.jetbrains.annotations.NotNull()
    java.lang.String value);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Long scard(@org.jetbrains.annotations.NotNull()
    java.lang.String key);
    
    public abstract boolean expire(@org.jetbrains.annotations.NotNull()
    java.lang.String key, long seconds);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object eval(@org.jetbrains.annotations.NotNull()
    java.lang.String script, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> keys, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> args);
    
    public abstract int zcount(@org.jetbrains.annotations.NotNull()
    java.lang.String key, long min, long max);
    
    public abstract long zadd(@org.jetbrains.annotations.NotNull()
    java.lang.String key, double score, @org.jetbrains.annotations.NotNull()
    java.lang.String member);
    
    public abstract long zremrangebyscore(@org.jetbrains.annotations.NotNull()
    java.lang.String key, double min, double max);
}