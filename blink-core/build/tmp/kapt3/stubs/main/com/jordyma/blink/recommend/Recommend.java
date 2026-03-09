package com.jordyma.blink.recommend;

@jakarta.persistence.Entity()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\r\b\u0007\u0018\u00002\u00020\u0001B)\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t\u00a2\u0006\u0002\u0010\nR\u001e\u0010\u0002\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u001a\u0010\b\u001a\u0004\u0018\u00010\t8\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u0013\u001a\u0004\b\u0011\u0010\u0012R\u0016\u0010\u0006\u001a\u00020\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015\u00a8\u0006\u0016"}, d2 = {"Lcom/jordyma/blink/recommend/Recommend;", "Lcom/jordyma/blink/common/BaseTimeEntity;", "feed", "Lcom/jordyma/blink/feed/domain/Feed;", "folderName", "", "priority", "", "id", "", "(Lcom/jordyma/blink/feed/domain/Feed;Ljava/lang/String;ILjava/lang/Long;)V", "getFeed", "()Lcom/jordyma/blink/feed/domain/Feed;", "setFeed", "(Lcom/jordyma/blink/feed/domain/Feed;)V", "getFolderName", "()Ljava/lang/String;", "getId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getPriority", "()I", "blink-core"})
public final class Recommend extends com.jordyma.blink.common.BaseTimeEntity {
    @jakarta.persistence.ManyToOne()
    @jakarta.persistence.JoinColumn(name = "feed_id")
    @org.jetbrains.annotations.NotNull()
    private com.jordyma.blink.feed.domain.Feed feed;
    @jakarta.persistence.Column(name = "folder_name", nullable = false)
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String folderName = null;
    @jakarta.persistence.Column(name = "priority", nullable = false)
    private final int priority = 0;
    @jakarta.persistence.Id()
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @jakarta.persistence.Column(name = "id")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long id = null;
    
    public Recommend(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.feed.domain.Feed feed, @org.jetbrains.annotations.NotNull()
    java.lang.String folderName, int priority, @org.jetbrains.annotations.Nullable()
    java.lang.Long id) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.jordyma.blink.feed.domain.Feed getFeed() {
        return null;
    }
    
    public final void setFeed(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.feed.domain.Feed p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFolderName() {
        return null;
    }
    
    public final int getPriority() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getId() {
        return null;
    }
}