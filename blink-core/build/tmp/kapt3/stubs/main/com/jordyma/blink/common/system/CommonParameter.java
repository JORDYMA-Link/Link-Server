package com.jordyma.blink.common.system;

@jakarta.persistence.Entity()
@jakarta.persistence.Table(name = "common_parameter", uniqueConstraints = {@jakarta.persistence.UniqueConstraint(columnNames = {"param_code", "param_value"})})
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\f\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0014\u00a2\u0006\u0002\u0010\u0002B-\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0004\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\u0002\u0010\tJ\u000e\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0004J\u000e\u0010\u001a\u001a\u00020\u00182\u0006\u0010\u001b\u001a\u00020\u0004R\u0016\u0010\n\u001a\u00020\u000b8\u0006X\u0087D\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0016\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0016\u0010\u0005\u001a\u00020\u00048\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000fR \u0010\b\u001a\u0004\u0018\u00010\u00078\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R \u0010\u0006\u001a\u0004\u0018\u00010\u00078\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0012\"\u0004\b\u0016\u0010\u0014\u00a8\u0006\u001c"}, d2 = {"Lcom/jordyma/blink/common/system/CommonParameter;", "", "()V", "paramCode", "", "paramValue", "validStartDate", "Ljava/time/LocalDate;", "validEndDate", "(Ljava/lang/String;Ljava/lang/String;Ljava/time/LocalDate;Ljava/time/LocalDate;)V", "id", "", "getId", "()J", "getParamCode", "()Ljava/lang/String;", "getParamValue", "getValidEndDate", "()Ljava/time/LocalDate;", "setValidEndDate", "(Ljava/time/LocalDate;)V", "getValidStartDate", "setValidStartDate", "updateEndDate", "", "endDate", "updateStartDate", "startDate", "blink-core"})
public final class CommonParameter {
    @jakarta.persistence.Column(name = "param_code", length = 50, nullable = false)
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String paramCode = null;
    @jakarta.persistence.Column(name = "param_value", nullable = false)
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String paramValue = null;
    @jakarta.persistence.Column(name = "valid_start_date")
    @org.jetbrains.annotations.Nullable()
    private java.time.LocalDate validStartDate;
    @jakarta.persistence.Column(name = "valid_end_date")
    @org.jetbrains.annotations.Nullable()
    private java.time.LocalDate validEndDate;
    @jakarta.persistence.Id()
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private final long id = 0L;
    
    public CommonParameter(@org.jetbrains.annotations.NotNull()
    java.lang.String paramCode, @org.jetbrains.annotations.NotNull()
    java.lang.String paramValue, @org.jetbrains.annotations.Nullable()
    java.time.LocalDate validStartDate, @org.jetbrains.annotations.Nullable()
    java.time.LocalDate validEndDate) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getParamCode() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getParamValue() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.time.LocalDate getValidStartDate() {
        return null;
    }
    
    public final void setValidStartDate(@org.jetbrains.annotations.Nullable()
    java.time.LocalDate p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.time.LocalDate getValidEndDate() {
        return null;
    }
    
    public final void setValidEndDate(@org.jetbrains.annotations.Nullable()
    java.time.LocalDate p0) {
    }
    
    public final long getId() {
        return 0L;
    }
    
    protected CommonParameter() {
        super();
    }
    
    public final void updateStartDate(@org.jetbrains.annotations.NotNull()
    java.lang.String startDate) {
    }
    
    public final void updateEndDate(@org.jetbrains.annotations.NotNull()
    java.lang.String endDate) {
    }
}