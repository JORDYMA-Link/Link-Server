package com.jordyma.blink.folder;

@org.springframework.stereotype.Repository()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\bg\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u00012\u00020\u0004J\u0016\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH&J\u001a\u0010\t\u001a\u0004\u0018\u00010\u00022\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\bH\'J\u001a\u0010\f\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000bH\'J\u0016\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\'J\u0012\u0010\u000f\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0007\u001a\u00020\bH\'\u00a8\u0006\u0010"}, d2 = {"Lcom/jordyma/blink/folder/FolderRepository;", "Lorg/springframework/data/jpa/repository/JpaRepository;", "Lcom/jordyma/blink/folder/Folder;", "", "Lcom/jordyma/blink/folder/CustomFolderRepository;", "findAllByUser", "", "user", "Lcom/jordyma/blink/user/User;", "findByName", "name", "", "findFailed", "status", "findFoldersByUser", "findUnclassified", "blink-core"})
public abstract interface FolderRepository extends org.springframework.data.jpa.repository.JpaRepository<com.jordyma.blink.folder.Folder, java.lang.Long>, com.jordyma.blink.folder.CustomFolderRepository {
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public abstract java.util.List<com.jordyma.blink.folder.Folder> findAllByUser(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.user.User user);
    
    @org.springframework.data.jpa.repository.Query(value = "select f from Folder f where f.user =:user and f.isUnclassified =true")
    @org.jetbrains.annotations.Nullable()
    public abstract com.jordyma.blink.folder.Folder findUnclassified(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.user.User user);
    
    @org.springframework.data.jpa.repository.Query(value = "select f from Folder f where f.user =:user and f.name =:status")
    @org.jetbrains.annotations.Nullable()
    public abstract com.jordyma.blink.folder.Folder findFailed(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.user.User user, @org.jetbrains.annotations.NotNull()
    java.lang.String status);
    
    @org.springframework.data.jpa.repository.Query(value = "select f from Folder f where f.user =:user and f.name =:name")
    @org.jetbrains.annotations.Nullable()
    public abstract com.jordyma.blink.folder.Folder findByName(@org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    com.jordyma.blink.user.User user);
    
    @org.springframework.data.jpa.repository.Query(value = "select f from Folder f where f.user =:user")
    @org.jetbrains.annotations.NotNull()
    public abstract java.util.List<com.jordyma.blink.folder.Folder> findFoldersByUser(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.user.User user);
}