package com.jordyma.blink.featureflag;

/**
 * 요약 처리 전략
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004\u00a8\u0006\u0005"}, d2 = {"Lcom/jordyma/blink/featureflag/SummarizationStrategy;", "", "(Ljava/lang/String;I)V", "V1_WORKER_SQS", "V2_API_DIRECT", "blink-core"})
public enum SummarizationStrategy {
    /*public static final*/ V1_WORKER_SQS /* = new V1_WORKER_SQS() */,
    /*public static final*/ V2_API_DIRECT /* = new V2_API_DIRECT() */;
    
    SummarizationStrategy() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.jordyma.blink.featureflag.SummarizationStrategy> getEntries() {
        return null;
    }
}