package com.jordyma.blink.folder.impl;

@org.springframework.stereotype.Repository()
@jakarta.transaction.Transactional()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0017\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0016\u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0092\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/jordyma/blink/folder/impl/CustomFolderRepositoryImpl;", "Lcom/jordyma/blink/folder/CustomFolderRepository;", "queryFactory", "Lcom/querydsl/jpa/impl/JPAQueryFactory;", "(Lcom/querydsl/jpa/impl/JPAQueryFactory;)V", "deleteFolder", "", "folder", "Lcom/jordyma/blink/folder/Folder;", "findAllByUser", "", "user", "Lcom/jordyma/blink/user/User;", "blink-core"})
public class CustomFolderRepositoryImpl implements com.jordyma.blink.folder.CustomFolderRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.querydsl.jpa.impl.JPAQueryFactory queryFactory = null;
    
    public CustomFolderRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.querydsl.jpa.impl.JPAQueryFactory queryFactory) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.util.List<com.jordyma.blink.folder.Folder> findAllByUser(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.user.User user) {
        return null;
    }
    
    @java.lang.Override()
    public void deleteFolder(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.folder.Folder folder) {
    }
}