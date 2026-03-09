package com.jordyma.blink.user;

@org.springframework.stereotype.Repository()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001J\u000e\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00020\u0005H\'J\u0012\u0010\u0006\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0007\u001a\u00020\bH\'J\u001a\u0010\t\u001a\u0004\u0018\u00010\u00022\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\bH&\u00a8\u0006\f"}, d2 = {"Lcom/jordyma/blink/user/UserRepository;", "Lorg/springframework/data/jpa/repository/JpaRepository;", "Lcom/jordyma/blink/user/User;", "", "findActiveMobileUser", "", "findAppleUser", "socialUserId", "", "findBySocialTypeAndSocialUserId", "socialType", "Lcom/jordyma/blink/user/SocialType;", "blink-core"})
public abstract interface UserRepository extends org.springframework.data.jpa.repository.JpaRepository<com.jordyma.blink.user.User, java.lang.Long> {
    
    @org.jetbrains.annotations.Nullable()
    public abstract com.jordyma.blink.user.User findBySocialTypeAndSocialUserId(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.user.SocialType socialType, @org.jetbrains.annotations.NotNull()
    java.lang.String socialUserId);
    
    @org.springframework.data.jpa.repository.Query(value = "select u from user u where u.socialType =\'APPLE\' and u.socialUserId =:socialUserId")
    @org.jetbrains.annotations.Nullable()
    public abstract com.jordyma.blink.user.User findAppleUser(@org.jetbrains.annotations.NotNull()
    java.lang.String socialUserId);
    
    @org.springframework.data.jpa.repository.Query(value = "select u from user u where u.deletedAt is null and (u.iosPushToken is not null or u.aosPushToken is not null)")
    @org.jetbrains.annotations.NotNull()
    public abstract java.util.List<com.jordyma.blink.user.User> findActiveMobileUser();
}