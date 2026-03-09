package com.jordyma.blink.folder.domain.service;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&J\"\u0010\b\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&J\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u0005H&J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u0005H&J \u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\u0011\u001a\u00020\u0012H&J\u0010\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\f\u001a\u00020\u0005H&J\u0016\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00030\u00152\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0016\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u0005H&J \u0010\u0018\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&\u00a8\u0006\u0019"}, d2 = {"Lcom/jordyma/blink/folder/domain/service/FolderService;", "", "create", "Lcom/jordyma/blink/folder/domain/model/FolderDto;", "userId", "", "folderName", "", "createFeedFolder", "feedId", "delete", "", "folderId", "getFailed", "Lcom/jordyma/blink/folder/Folder;", "getFeedsByFolder", "Lcom/jordyma/blink/folder/domain/model/GetFeedsByFolderResponseDto;", "request", "Lcom/jordyma/blink/folder/domain/model/GetFeedsByFolderRequestDto;", "getFolderById", "getFolders", "", "getUnclassified", "signOutDelete", "update", "blink-core"})
public abstract interface FolderService {
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.util.List<com.jordyma.blink.folder.domain.model.FolderDto> getFolders(long userId);
    
    public abstract void delete(long userId, long folderId);
    
    public abstract void signOutDelete(long userId);
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.jordyma.blink.folder.domain.model.GetFeedsByFolderResponseDto getFeedsByFolder(long userId, long folderId, @org.jetbrains.annotations.NotNull()
    com.jordyma.blink.folder.domain.model.GetFeedsByFolderRequestDto request);
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.jordyma.blink.folder.domain.model.FolderDto create(long userId, @org.jetbrains.annotations.NotNull()
    java.lang.String folderName);
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.jordyma.blink.folder.domain.model.FolderDto update(long userId, long folderId, @org.jetbrains.annotations.NotNull()
    java.lang.String folderName);
    
    @org.jetbrains.annotations.Nullable()
    public abstract com.jordyma.blink.folder.domain.model.FolderDto createFeedFolder(long userId, long feedId, @org.jetbrains.annotations.NotNull()
    java.lang.String folderName);
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.jordyma.blink.folder.Folder getUnclassified(long userId);
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.jordyma.blink.folder.Folder getFailed(long userId);
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.jordyma.blink.folder.Folder getFolderById(long folderId);
}