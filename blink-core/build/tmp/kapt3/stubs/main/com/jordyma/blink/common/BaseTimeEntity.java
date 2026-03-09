package com.jordyma.blink.common;

@jakarta.persistence.MappedSuperclass()
@jakarta.persistence.EntityListeners(value = {org.springframework.data.jpa.domain.support.AuditingEntityListener.class})
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0017\u0018\u00002\u00020\u0001B\u0011\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0004J\u0012\u0010\u000f\u001a\u00020\u00102\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0003J\u0012\u0010\u0012\u001a\u00020\u00102\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0003R \u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\u0004R*\u0010\t\u001a\u0004\u0018\u00010\u00032\b\u0010\b\u001a\u0004\u0018\u00010\u00038\u0006@DX\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\u0004R*\u0010\f\u001a\u0004\u0018\u00010\u00032\b\u0010\b\u001a\u0004\u0018\u00010\u00038\u0006@DX\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u0006\"\u0004\b\u000e\u0010\u0004\u00a8\u0006\u0013"}, d2 = {"Lcom/jordyma/blink/common/BaseTimeEntity;", "", "createdAt", "Ljava/time/LocalDateTime;", "(Ljava/time/LocalDateTime;)V", "getCreatedAt", "()Ljava/time/LocalDateTime;", "setCreatedAt", "<set-?>", "deletedAt", "getDeletedAt", "setDeletedAt", "updatedAt", "getUpdatedAt", "setUpdatedAt", "updateDeletedAt", "", "dateTime", "updateUpdatedAt", "blink-core"})
@lombok.Getter()
public class BaseTimeEntity {
    @org.springframework.data.annotation.CreatedDate()
    @jakarta.persistence.Column(name = "createdAt", updatable = false)
    @org.jetbrains.annotations.Nullable()
    private java.time.LocalDateTime createdAt;
    @org.springframework.data.annotation.LastModifiedDate()
    @jakarta.persistence.Column(name = "updatedAt")
    @org.jetbrains.annotations.Nullable()
    private java.time.LocalDateTime updatedAt;
    @jakarta.persistence.Column(name = "deletedAt")
    @org.jetbrains.annotations.Nullable()
    private java.time.LocalDateTime deletedAt;
    
    public BaseTimeEntity(@org.jetbrains.annotations.Nullable()
    java.time.LocalDateTime createdAt) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.time.LocalDateTime getCreatedAt() {
        return null;
    }
    
    public final void setCreatedAt(@org.jetbrains.annotations.Nullable()
    java.time.LocalDateTime p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.time.LocalDateTime getUpdatedAt() {
        return null;
    }
    
    protected final void setUpdatedAt(@org.jetbrains.annotations.Nullable()
    java.time.LocalDateTime p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.time.LocalDateTime getDeletedAt() {
        return null;
    }
    
    protected final void setDeletedAt(@org.jetbrains.annotations.Nullable()
    java.time.LocalDateTime p0) {
    }
    
    public final void updateUpdatedAt(@org.jetbrains.annotations.Nullable()
    java.time.LocalDateTime dateTime) {
    }
    
    public final void updateDeletedAt(@org.jetbrains.annotations.Nullable()
    java.time.LocalDateTime dateTime) {
    }
    
    public BaseTimeEntity() {
        super();
    }
}