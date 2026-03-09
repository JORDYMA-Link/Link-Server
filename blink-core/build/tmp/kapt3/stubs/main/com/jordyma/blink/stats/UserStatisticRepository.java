package com.jordyma.blink.stats;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001J\u001e\u0010\u0004\u001a\u0004\u0018\u00010\u00022\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0007\u001a\u00020\bH\'\u00a8\u0006\t"}, d2 = {"Lcom/jordyma/blink/stats/UserStatisticRepository;", "Lorg/springframework/data/jpa/repository/JpaRepository;", "Lcom/jordyma/blink/stats/UserStatistic;", "", "findByTypeAndDate", "type", "", "date", "Ljava/time/LocalDateTime;", "blink-core"})
public abstract interface UserStatisticRepository extends org.springframework.data.jpa.repository.JpaRepository<com.jordyma.blink.stats.UserStatistic, java.lang.Long> {
    
    @org.springframework.data.jpa.repository.Query(value = "SELECT u FROM UserStatistic u WHERE u.type = :type AND DATE(u.date) = DATE(:date)")
    @org.jetbrains.annotations.Nullable()
    public abstract com.jordyma.blink.stats.UserStatistic findByTypeAndDate(@org.springframework.data.repository.query.Param(value = "type")
    @org.jetbrains.annotations.NotNull()
    java.lang.String type, @org.springframework.data.repository.query.Param(value = "date")
    @org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime date);
}