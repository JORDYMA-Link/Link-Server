package com.jordyma.blink.config;

@org.springframework.context.annotation.Configuration()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0017\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0005\u001a\u00020\u0006H\u0017R\u0010\u0010\u0002\u001a\u00020\u00038\u0012X\u0093\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/jordyma/blink/config/QuerydslConfig;", "", "entityManager", "Ljakarta/persistence/EntityManager;", "(Ljakarta/persistence/EntityManager;)V", "jpaQueryFactory", "Lcom/querydsl/jpa/impl/JPAQueryFactory;", "blink-core"})
public class QuerydslConfig {
    @jakarta.persistence.PersistenceContext()
    @org.jetbrains.annotations.NotNull()
    private final jakarta.persistence.EntityManager entityManager = null;
    
    public QuerydslConfig(@org.jetbrains.annotations.NotNull()
    jakarta.persistence.EntityManager entityManager) {
        super();
    }
    
    @org.springframework.context.annotation.Bean()
    @org.jetbrains.annotations.NotNull()
    public com.querydsl.jpa.impl.JPAQueryFactory jpaQueryFactory() {
        return null;
    }
}