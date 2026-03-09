package com.jordyma.blink.folder;

@jakarta.persistence.Entity()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0012\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B3\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u000e\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u000bJ\u0006\u0010 \u001a\u00020\u001eJ\u0006\u0010!\u001a\u00020\u001eR\u001e\u0010\b\u001a\u00020\t8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u0013\u001a\u0004\b\u0011\u0010\u0012R\u001e\u0010\n\u001a\u00020\u000b8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001e\u0010\u0006\u001a\u00020\u00078\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001c\u00a8\u0006\""}, d2 = {"Lcom/jordyma/blink/folder/Folder;", "Lcom/jordyma/blink/common/BaseTimeEntity;", "id", "", "user", "Lcom/jordyma/blink/user/User;", "name", "", "count", "", "isUnclassified", "", "(Ljava/lang/Long;Lcom/jordyma/blink/user/User;Ljava/lang/String;IZ)V", "getCount", "()I", "setCount", "(I)V", "getId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "()Z", "setUnclassified", "(Z)V", "getName", "()Ljava/lang/String;", "setName", "(Ljava/lang/String;)V", "getUser", "()Lcom/jordyma/blink/user/User;", "changeIsUnclassified", "", "newIsUnclassified", "decreaseCount", "increaseCount", "blink-core"})
public final class Folder extends com.jordyma.blink.common.BaseTimeEntity {
    @jakarta.persistence.Id()
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @jakarta.persistence.Column(name = "id")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long id = null;
    @jakarta.persistence.ManyToOne()
    @jakarta.persistence.JoinColumn(name = "user_id")
    @org.jetbrains.annotations.NotNull()
    private final com.jordyma.blink.user.User user = null;
    @jakarta.persistence.Column(name = "name", length = 50)
    @org.jetbrains.annotations.NotNull()
    private java.lang.String name;
    @jakarta.persistence.Column(name = "count")
    private int count;
    @jakarta.persistence.Column(name = "is_unclassified", columnDefinition = "BIT")
    private boolean isUnclassified;
    
    public Folder(@org.jetbrains.annotations.Nullable()
    java.lang.Long id, @org.jetbrains.annotations.NotNull()
    com.jordyma.blink.user.User user, @org.jetbrains.annotations.NotNull()
    java.lang.String name, int count, boolean isUnclassified) {
        super(null);
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.jordyma.blink.user.User getUser() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getName() {
        return null;
    }
    
    public final void setName(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    public final int getCount() {
        return 0;
    }
    
    public final void setCount(int p0) {
    }
    
    public final boolean isUnclassified() {
        return false;
    }
    
    public final void setUnclassified(boolean p0) {
    }
    
    public final void changeIsUnclassified(boolean newIsUnclassified) {
    }
    
    public final void increaseCount() {
    }
    
    public final void decreaseCount() {
    }
}