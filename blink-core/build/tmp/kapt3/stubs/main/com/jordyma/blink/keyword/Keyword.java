package com.jordyma.blink.keyword;

@jakarta.persistence.Entity()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\b\u0007\u0018\u00002\u00020\u0001B!\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bR\u0016\u0010\u0006\u001a\u00020\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u001a\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u000f\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u0010"}, d2 = {"Lcom/jordyma/blink/keyword/Keyword;", "Lcom/jordyma/blink/common/BaseTimeEntity;", "id", "", "feed", "Lcom/jordyma/blink/feed/domain/Feed;", "content", "", "(Ljava/lang/Long;Lcom/jordyma/blink/feed/domain/Feed;Ljava/lang/String;)V", "getContent", "()Ljava/lang/String;", "getFeed", "()Lcom/jordyma/blink/feed/domain/Feed;", "getId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "blink-core"})
public final class Keyword extends com.jordyma.blink.common.BaseTimeEntity {
    @jakarta.persistence.Id()
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @jakarta.persistence.Column(name = "id")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long id = null;
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "feed_id")
    @org.jetbrains.annotations.NotNull()
    private final com.jordyma.blink.feed.domain.Feed feed = null;
    @jakarta.persistence.Column(name = "content", length = 20)
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String content = null;
    
    public Keyword(@org.jetbrains.annotations.Nullable()
    java.lang.Long id, @org.jetbrains.annotations.NotNull()
    com.jordyma.blink.feed.domain.Feed feed, @org.jetbrains.annotations.NotNull()
    java.lang.String content) {
        super(null);
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.jordyma.blink.feed.domain.Feed getFeed() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getContent() {
        return null;
    }
}