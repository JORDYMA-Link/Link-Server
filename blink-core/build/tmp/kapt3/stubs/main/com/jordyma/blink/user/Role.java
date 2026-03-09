package com.jordyma.blink.user;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u00012\u00020\u0002B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0016j\u0002\b\u0006j\u0002\b\u0007\u00a8\u0006\b"}, d2 = {"Lcom/jordyma/blink/user/Role;", "", "Lorg/springframework/security/core/GrantedAuthority;", "(Ljava/lang/String;I)V", "getAuthority", "", "ADMIN", "USER", "blink-core"})
public enum Role implements org.springframework.security.core.GrantedAuthority {
    /*public static final*/ ADMIN /* = new ADMIN() */,
    /*public static final*/ USER /* = new USER() */;
    
    Role() {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String getAuthority() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.jordyma.blink.user.Role> getEntries() {
        return null;
    }
}