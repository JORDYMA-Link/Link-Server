package com.jordyma.blink.user;

@org.springframework.stereotype.Repository()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\bg\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001J\u0014\u0010\u0004\u001a\u0004\u0018\u00010\u00022\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H&J\u0016\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00020\b2\u0006\u0010\t\u001a\u00020\u0003H\'\u00a8\u0006\n"}, d2 = {"Lcom/jordyma/blink/user/UserRefreshTokenRepository;", "Lorg/springframework/data/jpa/repository/JpaRepository;", "Lcom/jordyma/blink/user/UserRefreshToken;", "", "findByRefreshToken", "refreshToken", "", "findByUserId", "", "id", "blink-core"})
public abstract interface UserRefreshTokenRepository extends org.springframework.data.jpa.repository.JpaRepository<com.jordyma.blink.user.UserRefreshToken, java.lang.Long> {
    
    @org.jetbrains.annotations.Nullable()
    public abstract com.jordyma.blink.user.UserRefreshToken findByRefreshToken(@org.jetbrains.annotations.Nullable()
    java.lang.String refreshToken);
    
    @org.springframework.data.jpa.repository.Query(value = "select urt from UserRefreshToken urt where urt.user.id =:id")
    @org.jetbrains.annotations.NotNull()
    public abstract java.util.List<com.jordyma.blink.user.UserRefreshToken> findByUserId(long id);
}