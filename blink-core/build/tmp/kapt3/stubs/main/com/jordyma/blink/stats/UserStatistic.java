package com.jordyma.blink.stats;

@jakarta.persistence.Entity()
@jakarta.persistence.Table(name = "user_statistic")
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B)\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\b\u0010\u0019\u001a\u00020\u0005H\u0016J\u000e\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u0003R\u001e\u0010\u0006\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001e\u0010\u0007\u001a\u00020\b8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\"\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0010\n\u0002\u0010\u0016\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018\u00a8\u0006\u001d"}, d2 = {"Lcom/jordyma/blink/stats/UserStatistic;", "", "id", "", "type", "", "count", "date", "Ljava/time/LocalDateTime;", "(Ljava/lang/Long;Ljava/lang/String;JLjava/time/LocalDateTime;)V", "getCount", "()J", "setCount", "(J)V", "getDate", "()Ljava/time/LocalDateTime;", "setDate", "(Ljava/time/LocalDateTime;)V", "getId", "()Ljava/lang/Long;", "setId", "(Ljava/lang/Long;)V", "Ljava/lang/Long;", "getType", "()Ljava/lang/String;", "toString", "updateCount", "", "newCount", "blink-core"})
public final class UserStatistic {
    @jakarta.persistence.Id()
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @jakarta.persistence.Column(name = "id")
    @org.jetbrains.annotations.Nullable()
    private java.lang.Long id;
    @jakarta.persistence.Column(name = "type", nullable = false)
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String type = null;
    @jakarta.persistence.Column(name = "count", nullable = false)
    private long count;
    @jakarta.persistence.Column(name = "date", nullable = false)
    @org.jetbrains.annotations.NotNull()
    private java.time.LocalDateTime date;
    
    public UserStatistic(@org.jetbrains.annotations.Nullable()
    java.lang.Long id, @org.jetbrains.annotations.NotNull()
    java.lang.String type, long count, @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime date) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getId() {
        return null;
    }
    
    public final void setId(@org.jetbrains.annotations.Nullable()
    java.lang.Long p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getType() {
        return null;
    }
    
    public final long getCount() {
        return 0L;
    }
    
    public final void setCount(long p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDateTime getDate() {
        return null;
    }
    
    public final void setDate(@org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime p0) {
    }
    
    public final void updateCount(long newCount) {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}