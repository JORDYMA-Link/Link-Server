package com.jordyma.blink.message;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\bf\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001J\u0014\u0010\u0004\u001a\u0004\u0018\u00010\u00022\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\'\u00a8\u0006\u0007"}, d2 = {"Lcom/jordyma/blink/message/MessageRepository;", "Lorg/springframework/data/jpa/repository/JpaRepository;", "Lcom/jordyma/blink/message/PushMessage;", "", "findPendingMessages", "now", "", "blink-core"})
public abstract interface MessageRepository extends org.springframework.data.jpa.repository.JpaRepository<com.jordyma.blink.message.PushMessage, java.lang.Long> {
    
    @org.springframework.data.jpa.repository.Query(value = "select m from PushMessage m where m.date =:now")
    @org.jetbrains.annotations.Nullable()
    public abstract com.jordyma.blink.message.PushMessage findPendingMessages(@org.jetbrains.annotations.Nullable()
    java.lang.String now);
}