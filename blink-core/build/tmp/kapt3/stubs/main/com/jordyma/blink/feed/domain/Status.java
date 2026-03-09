package com.jordyma.blink.feed.domain;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007\u00a8\u0006\b"}, d2 = {"Lcom/jordyma/blink/feed/domain/Status;", "", "(Ljava/lang/String;I)V", "REQUESTED", "PROCESSING", "COMPLETED", "SAVED", "FAILED", "blink-core"})
public enum Status {
    /*public static final*/ REQUESTED /* = new REQUESTED() */,
    /*public static final*/ PROCESSING /* = new PROCESSING() */,
    /*public static final*/ COMPLETED /* = new COMPLETED() */,
    /*public static final*/ SAVED /* = new SAVED() */,
    /*public static final*/ FAILED /* = new FAILED() */;
    
    Status() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.jordyma.blink.feed.domain.Status> getEntries() {
        return null;
    }
}