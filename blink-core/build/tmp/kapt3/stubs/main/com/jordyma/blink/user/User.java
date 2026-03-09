package com.jordyma.blink.user;

@jakarta.persistence.Entity(name = "user")
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\t\n\u0002\b\u0016\n\u0002\u0010\u0002\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B{\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000f\u00a2\u0006\u0002\u0010\u0010J\u000e\u00100\u001a\u0002012\u0006\u0010\r\u001a\u00020\u0003J\u0006\u00102\u001a\u000201J\u000e\u00103\u001a\u0002012\u0006\u0010\u000b\u001a\u00020\u0003J\u000e\u00104\u001a\u0002012\u0006\u0010\f\u001a\u00020\u0003J\u000e\u00105\u001a\u0002012\u0006\u0010\u000e\u001a\u00020\u0003J\u000e\u00106\u001a\u0002012\u0006\u0010\u0002\u001a\u00020\u0003J\u0006\u00107\u001a\u000201R \u0010\n\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R \u0010\r\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0012\"\u0004\b\u0016\u0010\u0014R \u0010\u000b\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0012\"\u0004\b\u0018\u0010\u0014R\u001a\u0010\u0019\u001a\u0004\u0018\u00010\u001a8\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u001d\u001a\u0004\b\u001b\u0010\u001cR \u0010\t\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u0012\"\u0004\b\u001f\u0010\u0014R \u0010\f\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u0012\"\u0004\b!\u0010\u0014R \u0010\u000e\u001a\u0004\u0018\u00010\u000f8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%R\u001e\u0010\u0002\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b&\u0010\u0012\"\u0004\b\'\u0010\u0014R \u0010\u0007\u001a\u0004\u0018\u00010\b8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b(\u0010)\"\u0004\b*\u0010+R\u0018\u0010\u0004\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010-R \u0010\u0006\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b.\u0010\u0012\"\u0004\b/\u0010\u0014\u00a8\u00068"}, d2 = {"Lcom/jordyma/blink/user/User;", "Lcom/jordyma/blink/common/BaseTimeEntity;", "nickname", "", "socialType", "Lcom/jordyma/blink/user/SocialType;", "socialUserId", "role", "Lcom/jordyma/blink/user/Role;", "iosPushToken", "aosPushToken", "gender", "jobField", "birthYear", "language", "Lcom/jordyma/blink/user/LanguageType;", "(Ljava/lang/String;Lcom/jordyma/blink/user/SocialType;Ljava/lang/String;Lcom/jordyma/blink/user/Role;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/jordyma/blink/user/LanguageType;)V", "getAosPushToken", "()Ljava/lang/String;", "setAosPushToken", "(Ljava/lang/String;)V", "getBirthYear", "setBirthYear", "getGender", "setGender", "id", "", "getId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getIosPushToken", "setIosPushToken", "getJobField", "setJobField", "getLanguage", "()Lcom/jordyma/blink/user/LanguageType;", "setLanguage", "(Lcom/jordyma/blink/user/LanguageType;)V", "getNickname", "setNickname", "getRole", "()Lcom/jordyma/blink/user/Role;", "setRole", "(Lcom/jordyma/blink/user/Role;)V", "getSocialType", "()Lcom/jordyma/blink/user/SocialType;", "getSocialUserId", "setSocialUserId", "updateBirthYear", "", "updateDeletedAt", "updateGender", "updateJobField", "updateLanguage", "updateNickname", "updateSocialId", "blink-core"})
public final class User extends com.jordyma.blink.common.BaseTimeEntity {
    @jakarta.persistence.Column(name = "nickname")
    @org.jetbrains.annotations.NotNull()
    private java.lang.String nickname;
    @jakarta.persistence.Column(name = "socialType")
    @jakarta.persistence.Enumerated(value = jakarta.persistence.EnumType.STRING)
    @org.jetbrains.annotations.Nullable()
    private final com.jordyma.blink.user.SocialType socialType = null;
    @jakarta.persistence.Column(name = "socialUserId")
    @org.jetbrains.annotations.Nullable()
    private java.lang.String socialUserId;
    @jakarta.persistence.Column(name = "role")
    @jakarta.persistence.Enumerated(value = jakarta.persistence.EnumType.STRING)
    @org.jetbrains.annotations.Nullable()
    private com.jordyma.blink.user.Role role;
    @jakarta.persistence.Column(name = "iosPushToken")
    @org.jetbrains.annotations.Nullable()
    private java.lang.String iosPushToken;
    @jakarta.persistence.Column(name = "aosPushToken")
    @org.jetbrains.annotations.Nullable()
    private java.lang.String aosPushToken;
    @jakarta.persistence.Column(name = "gender")
    @org.jetbrains.annotations.Nullable()
    private java.lang.String gender;
    @jakarta.persistence.Column(name = "jobField")
    @org.jetbrains.annotations.Nullable()
    private java.lang.String jobField;
    @jakarta.persistence.Column(name = "birthYear")
    @org.jetbrains.annotations.Nullable()
    private java.lang.String birthYear;
    @jakarta.persistence.Column(name = "language")
    @jakarta.persistence.Enumerated(value = jakarta.persistence.EnumType.STRING)
    @org.jetbrains.annotations.Nullable()
    private com.jordyma.blink.user.LanguageType language;
    @jakarta.persistence.Id()
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @jakarta.persistence.Column(name = "id")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long id = null;
    
    public User(@org.jetbrains.annotations.NotNull()
    java.lang.String nickname, @org.jetbrains.annotations.Nullable()
    com.jordyma.blink.user.SocialType socialType, @org.jetbrains.annotations.Nullable()
    java.lang.String socialUserId, @org.jetbrains.annotations.Nullable()
    com.jordyma.blink.user.Role role, @org.jetbrains.annotations.Nullable()
    java.lang.String iosPushToken, @org.jetbrains.annotations.Nullable()
    java.lang.String aosPushToken, @org.jetbrains.annotations.Nullable()
    java.lang.String gender, @org.jetbrains.annotations.Nullable()
    java.lang.String jobField, @org.jetbrains.annotations.Nullable()
    java.lang.String birthYear, @org.jetbrains.annotations.Nullable()
    com.jordyma.blink.user.LanguageType language) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getNickname() {
        return null;
    }
    
    public final void setNickname(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.jordyma.blink.user.SocialType getSocialType() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSocialUserId() {
        return null;
    }
    
    public final void setSocialUserId(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.jordyma.blink.user.Role getRole() {
        return null;
    }
    
    public final void setRole(@org.jetbrains.annotations.Nullable()
    com.jordyma.blink.user.Role p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getIosPushToken() {
        return null;
    }
    
    public final void setIosPushToken(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getAosPushToken() {
        return null;
    }
    
    public final void setAosPushToken(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getGender() {
        return null;
    }
    
    public final void setGender(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getJobField() {
        return null;
    }
    
    public final void setJobField(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getBirthYear() {
        return null;
    }
    
    public final void setBirthYear(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.jordyma.blink.user.LanguageType getLanguage() {
        return null;
    }
    
    public final void setLanguage(@org.jetbrains.annotations.Nullable()
    com.jordyma.blink.user.LanguageType p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getId() {
        return null;
    }
    
    public final void updateNickname(@org.jetbrains.annotations.NotNull()
    java.lang.String nickname) {
    }
    
    public final void updateDeletedAt() {
    }
    
    public final void updateSocialId() {
    }
    
    public final void updateJobField(@org.jetbrains.annotations.NotNull()
    java.lang.String jobField) {
    }
    
    public final void updateGender(@org.jetbrains.annotations.NotNull()
    java.lang.String gender) {
    }
    
    public final void updateBirthYear(@org.jetbrains.annotations.NotNull()
    java.lang.String birthYear) {
    }
    
    public final void updateLanguage(@org.jetbrains.annotations.NotNull()
    java.lang.String language) {
    }
    
    public User() {
        super(null);
    }
}