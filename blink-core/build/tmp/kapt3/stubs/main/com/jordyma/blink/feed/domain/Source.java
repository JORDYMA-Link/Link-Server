package com.jordyma.blink.feed.domain;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0012\b\u0086\u0081\u0002\u0018\u0000 \u00142\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\u0014B\u0017\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007j\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010j\u0002\b\u0011j\u0002\b\u0012j\u0002\b\u0013\u00a8\u0006\u0015"}, d2 = {"Lcom/jordyma/blink/feed/domain/Source;", "", "source", "", "image", "(Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;)V", "getImage", "()Ljava/lang/String;", "getSource", "NAVER_BLOG", "VELOG", "BRUNCH", "YOZM_IT", "TISTORY", "EO", "NAVER", "YOUTUBE", "GOOGLE", "DEFAULT", "ONBOARDING", "Companion", "blink-core"})
public enum Source {
    /*public static final*/ NAVER_BLOG /* = new NAVER_BLOG(null, null) */,
    /*public static final*/ VELOG /* = new VELOG(null, null) */,
    /*public static final*/ BRUNCH /* = new BRUNCH(null, null) */,
    /*public static final*/ YOZM_IT /* = new YOZM_IT(null, null) */,
    /*public static final*/ TISTORY /* = new TISTORY(null, null) */,
    /*public static final*/ EO /* = new EO(null, null) */,
    /*public static final*/ NAVER /* = new NAVER(null, null) */,
    /*public static final*/ YOUTUBE /* = new YOUTUBE(null, null) */,
    /*public static final*/ GOOGLE /* = new GOOGLE(null, null) */,
    /*public static final*/ DEFAULT /* = new DEFAULT(null, null) */,
    /*public static final*/ ONBOARDING /* = new ONBOARDING(null, null) */;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String source = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String image = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.jordyma.blink.feed.domain.Source.Companion Companion = null;
    
    Source(java.lang.String source, java.lang.String image) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSource() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getImage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.jordyma.blink.feed.domain.Source> getEntries() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0004J\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u00072\u0006\u0010\b\u001a\u00020\u0004J\u0010\u0010\t\u001a\u0004\u0018\u00010\u00042\u0006\u0010\b\u001a\u00020\u0004\u00a8\u0006\n"}, d2 = {"Lcom/jordyma/blink/feed/domain/Source$Companion;", "", "()V", "getBrunchByImage", "", "imageUrl", "getBrunchByName", "Lcom/jordyma/blink/feed/domain/Source;", "brunchName", "getImageByName", "blink-core"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getBrunchByImage(@org.jetbrains.annotations.NotNull()
        java.lang.String imageUrl) {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.jordyma.blink.feed.domain.Source getBrunchByName(@org.jetbrains.annotations.NotNull()
        java.lang.String brunchName) {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getImageByName(@org.jetbrains.annotations.NotNull()
        java.lang.String brunchName) {
            return null;
        }
    }
}