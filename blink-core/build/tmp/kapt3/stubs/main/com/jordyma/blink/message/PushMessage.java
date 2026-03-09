package com.jordyma.blink.message;

@jakarta.persistence.Entity()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\f\b\u0007\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\bR\u0016\u0010\u0006\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001e\u0010\u0007\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\n\"\u0004\b\f\u0010\rR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\n\u00a8\u0006\u0011"}, d2 = {"Lcom/jordyma/blink/message/PushMessage;", "", "id", "", "title", "", "body", "date", "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getBody", "()Ljava/lang/String;", "getDate", "setDate", "(Ljava/lang/String;)V", "getId", "()J", "getTitle", "blink-core"})
public final class PushMessage {
    @jakarta.persistence.Id()
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @jakarta.persistence.Column(name = "id")
    private final long id = 0L;
    @jakarta.persistence.Column(name = "title", length = 100)
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String title = null;
    @jakarta.persistence.Column(name = "body", length = 100)
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String body = null;
    @jakarta.persistence.Column(name = "date", length = 20)
    @org.jetbrains.annotations.NotNull()
    private java.lang.String date;
    
    public PushMessage(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String body, @org.jetbrains.annotations.NotNull()
    java.lang.String date) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getBody() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDate() {
        return null;
    }
    
    public final void setDate(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
}