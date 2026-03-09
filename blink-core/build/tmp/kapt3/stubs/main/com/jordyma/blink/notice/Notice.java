package com.jordyma.blink.notice;

@jakarta.persistence.Entity()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007R\u0016\u0010\u0004\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0016\u0010\u0005\u001a\u00020\u00068\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\t\u00a8\u0006\r"}, d2 = {"Lcom/jordyma/blink/notice/Notice;", "Lcom/jordyma/blink/common/BaseTimeEntity;", "title", "", "content", "id", "", "(Ljava/lang/String;Ljava/lang/String;J)V", "getContent", "()Ljava/lang/String;", "getId", "()J", "getTitle", "blink-core"})
public final class Notice extends com.jordyma.blink.common.BaseTimeEntity {
    @jakarta.persistence.Column(nullable = false, length = 50)
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String title = null;
    @jakarta.persistence.Column(nullable = false, length = 1000)
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String content = null;
    @jakarta.persistence.Id()
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private final long id = 0L;
    
    public Notice(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String content, long id) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getContent() {
        return null;
    }
    
    public final long getId() {
        return 0L;
    }
}