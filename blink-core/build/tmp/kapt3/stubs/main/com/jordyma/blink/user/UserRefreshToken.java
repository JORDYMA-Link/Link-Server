package com.jordyma.blink.user;

@jakarta.persistence.Entity()
@jakarta.persistence.Table(name = "user_refresh_token")
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0007\u0018\u0000 \u001d2\u00020\u0001:\u0001\u001dB5\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t\u00a2\u0006\u0002\u0010\nJ\u0006\u0010\u001a\u001a\u00020\u001bJ\u0010\u0010\u001c\u001a\u00020\u001b2\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005R\u001a\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\r\u001a\u0004\b\u000b\u0010\fR \u0010\u0004\u001a\u0004\u0018\u00010\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R \u0010\b\u001a\u0004\u0018\u00010\t8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R \u0010\u0006\u001a\u0004\u0018\u00010\u00078\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019\u00a8\u0006\u001e"}, d2 = {"Lcom/jordyma/blink/user/UserRefreshToken;", "Lcom/jordyma/blink/common/BaseTimeEntity;", "id", "", "refreshToken", "", "user", "Lcom/jordyma/blink/user/User;", "tokenExpirationTime", "Ljava/time/LocalDateTime;", "(Ljava/lang/Long;Ljava/lang/String;Lcom/jordyma/blink/user/User;Ljava/time/LocalDateTime;)V", "getId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getRefreshToken", "()Ljava/lang/String;", "setRefreshToken", "(Ljava/lang/String;)V", "getTokenExpirationTime", "()Ljava/time/LocalDateTime;", "setTokenExpirationTime", "(Ljava/time/LocalDateTime;)V", "getUser", "()Lcom/jordyma/blink/user/User;", "setUser", "(Lcom/jordyma/blink/user/User;)V", "expire", "", "updateRefreshToken", "Companion", "blink-core"})
public final class UserRefreshToken extends com.jordyma.blink.common.BaseTimeEntity {
    @jakarta.persistence.Id()
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @jakarta.persistence.Column(name = "id")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long id = null;
    @jakarta.persistence.Column(name = "refresh_token", columnDefinition = "VARCHAR(500)")
    @org.jetbrains.annotations.Nullable()
    private java.lang.String refreshToken;
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "user_id")
    @org.jetbrains.annotations.Nullable()
    private com.jordyma.blink.user.User user;
    @jakarta.persistence.Column(name = "token_expiration_time", columnDefinition = "DATETIME")
    @org.jetbrains.annotations.Nullable()
    private java.time.LocalDateTime tokenExpirationTime;
    @org.jetbrains.annotations.NotNull()
    public static final com.jordyma.blink.user.UserRefreshToken.Companion Companion = null;
    
    public UserRefreshToken(@org.jetbrains.annotations.Nullable()
    java.lang.Long id, @org.jetbrains.annotations.Nullable()
    java.lang.String refreshToken, @org.jetbrains.annotations.Nullable()
    com.jordyma.blink.user.User user, @org.jetbrains.annotations.Nullable()
    java.time.LocalDateTime tokenExpirationTime) {
        super(null);
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getId() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getRefreshToken() {
        return null;
    }
    
    public final void setRefreshToken(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.jordyma.blink.user.User getUser() {
        return null;
    }
    
    public final void setUser(@org.jetbrains.annotations.Nullable()
    com.jordyma.blink.user.User p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.time.LocalDateTime getTokenExpirationTime() {
        return null;
    }
    
    public final void setTokenExpirationTime(@org.jetbrains.annotations.Nullable()
    java.time.LocalDateTime p0) {
    }
    
    public final void updateRefreshToken(@org.jetbrains.annotations.Nullable()
    java.lang.String refreshToken) {
    }
    
    public final void expire() {
    }
    
    public UserRefreshToken() {
        super(null);
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\"\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\n\u00a8\u0006\u000b"}, d2 = {"Lcom/jordyma/blink/user/UserRefreshToken$Companion;", "", "()V", "of", "Lcom/jordyma/blink/user/UserRefreshToken;", "refreshToken", "", "user", "Lcom/jordyma/blink/user/User;", "expirationTime", "Ljava/time/LocalDateTime;", "blink-core"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.jordyma.blink.user.UserRefreshToken of(@org.jetbrains.annotations.Nullable()
        java.lang.String refreshToken, @org.jetbrains.annotations.Nullable()
        com.jordyma.blink.user.User user, @org.jetbrains.annotations.NotNull()
        java.time.LocalDateTime expirationTime) {
            return null;
        }
    }
}