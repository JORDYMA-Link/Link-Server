package com.jordyma.blink.recommend;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0000\bf\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001J\u0018\u0010\u0004\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0007H\'J\u0016\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00020\t2\u0006\u0010\u0005\u001a\u00020\u0003H\'\u00a8\u0006\n"}, d2 = {"Lcom/jordyma/blink/recommend/RecommendRepository;", "Lorg/springframework/data/jpa/repository/JpaRepository;", "Lcom/jordyma/blink/recommend/Recommend;", "", "findRecommendFirst", "feedId", "priority", "", "findRecommendationsByFeedId", "", "blink-core"})
public abstract interface RecommendRepository extends org.springframework.data.jpa.repository.JpaRepository<com.jordyma.blink.recommend.Recommend, java.lang.Long> {
    
    @org.springframework.data.jpa.repository.Query(value = "SELECT r FROM Recommend r WHERE r.feed.id = :feedId")
    @org.jetbrains.annotations.NotNull()
    public abstract java.util.List<com.jordyma.blink.recommend.Recommend> findRecommendationsByFeedId(long feedId);
    
    @org.springframework.data.jpa.repository.Query(value = "select r from Recommend r where r.feed.id =:feedId and r.priority =:priority")
    @org.jetbrains.annotations.NotNull()
    public abstract com.jordyma.blink.recommend.Recommend findRecommendFirst(long feedId, int priority);
}