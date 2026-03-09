package com.jordyma.blink.feed.domain;

@jakarta.persistence.Entity()
@jakarta.persistence.Table(name = "feed")
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b%\n\u0002\u0010\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0095\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\t\u001a\u00020\u0005\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u0012\b\b\u0002\u0010\r\u001a\u00020\f\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u000f\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0011\u0012\u000e\b\u0002\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013\u0012\u000e\b\u0002\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u0013\u00a2\u0006\u0002\u0010\u0017J\u0016\u0010;\u001a\u00020<2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u000e\u001a\u00020\u000fJ&\u0010;\u001a\u00020<2\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010=\u001a\u00020<2\u0006\u0010\u0010\u001a\u00020\u0011J\u0006\u0010>\u001a\u00020<J\u000e\u0010?\u001a\u00020<2\u0006\u0010@\u001a\u00020\fJ\u0014\u0010A\u001a\u00020<2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013J\u000e\u0010B\u001a\u00020<2\u0006\u0010\b\u001a\u00020\u0005J\u000e\u0010C\u001a\u00020<2\u0006\u0010\u000e\u001a\u00020\u000fJ\u001e\u0010D\u001a\u00020<2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010E\u001a\u00020FJ\u000e\u0010G\u001a\u00020<2\u0006\u0010H\u001a\u00020\u0005R \u0010\u0010\u001a\u0004\u0018\u00010\u00118\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR\u001e\u0010\u0002\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\u001e\u0010\r\u001a\u00020\f8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010 \"\u0004\b!\u0010\"R\u001e\u0010\u000b\u001a\u00020\f8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010 \"\u0004\b#\u0010\"R$\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u00138\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b$\u0010%\"\u0004\b&\u0010\'R \u0010\b\u001a\u0004\u0018\u00010\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b(\u0010)\"\u0004\b*\u0010+R\u0016\u0010\t\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010)R \u0010\u0007\u001a\u0004\u0018\u00010\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b-\u0010)\"\u0004\b.\u0010+R$\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u00138\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b/\u0010%\"\u0004\b0\u0010\'R\u001e\u0010\u000e\u001a\u00020\u000f8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b1\u00102\"\u0004\b3\u00104R\u001e\u0010\u0004\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b5\u0010)\"\u0004\b6\u0010+R \u0010\n\u001a\u0004\u0018\u00010\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b7\u0010)\"\u0004\b8\u0010+R\u001e\u0010\u0006\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b9\u0010)\"\u0004\b:\u0010+\u00a8\u0006I"}, d2 = {"Lcom/jordyma/blink/feed/domain/Feed;", "Lcom/jordyma/blink/common/BaseTimeEntity;", "id", "", "summary", "", "title", "platform", "memo", "originUrl", "thumbnailImageUrl", "isMarked", "", "isChecked", "status", "Lcom/jordyma/blink/feed/domain/Status;", "folder", "Lcom/jordyma/blink/folder/Folder;", "keywords", "", "Lcom/jordyma/blink/keyword/Keyword;", "recommendFolders", "Lcom/jordyma/blink/recommend/Recommend;", "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZZLcom/jordyma/blink/feed/domain/Status;Lcom/jordyma/blink/folder/Folder;Ljava/util/List;Ljava/util/List;)V", "getFolder", "()Lcom/jordyma/blink/folder/Folder;", "setFolder", "(Lcom/jordyma/blink/folder/Folder;)V", "getId", "()J", "setId", "(J)V", "()Z", "setChecked", "(Z)V", "setMarked", "getKeywords", "()Ljava/util/List;", "setKeywords", "(Ljava/util/List;)V", "getMemo", "()Ljava/lang/String;", "setMemo", "(Ljava/lang/String;)V", "getOriginUrl", "getPlatform", "setPlatform", "getRecommendFolders", "setRecommendFolders", "getStatus", "()Lcom/jordyma/blink/feed/domain/Status;", "setStatus", "(Lcom/jordyma/blink/feed/domain/Status;)V", "getSummary", "setSummary", "getThumbnailImageUrl", "setThumbnailImageUrl", "getTitle", "setTitle", "update", "", "updateFolder", "updateIsChecked", "updateIsMarked", "newIsMarked", "updateKeywords", "updateMemo", "updateStatus", "updateSummarizedContent", "brunch", "Lcom/jordyma/blink/feed/domain/Source;", "updateThumbnailImageUrl", "imageUrl", "blink-core"})
public final class Feed extends com.jordyma.blink.common.BaseTimeEntity {
    @jakarta.persistence.Id()
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private long id;
    @jakarta.persistence.Column(name = "summary", length = 200)
    @org.jetbrains.annotations.NotNull()
    private java.lang.String summary;
    @jakarta.persistence.Column(name = "title", length = 100)
    @org.jetbrains.annotations.NotNull()
    private java.lang.String title;
    @jakarta.persistence.Column(name = "platform", length = 100)
    @org.jetbrains.annotations.Nullable()
    private java.lang.String platform;
    @jakarta.persistence.Column(name = "memo", columnDefinition = "TEXT")
    @org.jetbrains.annotations.Nullable()
    private java.lang.String memo;
    @jakarta.persistence.Column(name = "origin_url", length = 512)
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String originUrl = null;
    @jakarta.persistence.Column(name = "thumbnail_image_url", length = 200)
    @org.jetbrains.annotations.Nullable()
    private java.lang.String thumbnailImageUrl;
    @jakarta.persistence.Column(name = "is_marked", columnDefinition = "BIT")
    private boolean isMarked;
    @jakarta.persistence.Column(name = "is_checked", columnDefinition = "BIT")
    private boolean isChecked;
    @jakarta.persistence.Column(name = "status", length = 10)
    @jakarta.persistence.Enumerated(value = jakarta.persistence.EnumType.STRING)
    @org.jetbrains.annotations.NotNull()
    private com.jordyma.blink.feed.domain.Status status;
    @jakarta.persistence.ManyToOne(cascade = {jakarta.persistence.CascadeType.PERSIST}, fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "folder_id")
    @org.jetbrains.annotations.Nullable()
    private com.jordyma.blink.folder.Folder folder;
    @jakarta.persistence.OneToMany(mappedBy = "feed")
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.jordyma.blink.keyword.Keyword> keywords;
    @jakarta.persistence.OneToMany(mappedBy = "feed")
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.jordyma.blink.recommend.Recommend> recommendFolders;
    
    public Feed(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String summary, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.Nullable()
    java.lang.String platform, @org.jetbrains.annotations.Nullable()
    java.lang.String memo, @org.jetbrains.annotations.NotNull()
    java.lang.String originUrl, @org.jetbrains.annotations.Nullable()
    java.lang.String thumbnailImageUrl, boolean isMarked, boolean isChecked, @org.jetbrains.annotations.NotNull()
    com.jordyma.blink.feed.domain.Status status, @org.jetbrains.annotations.Nullable()
    com.jordyma.blink.folder.Folder folder, @org.jetbrains.annotations.NotNull()
    java.util.List<com.jordyma.blink.keyword.Keyword> keywords, @org.jetbrains.annotations.NotNull()
    java.util.List<com.jordyma.blink.recommend.Recommend> recommendFolders) {
        super(null);
    }
    
    public final long getId() {
        return 0L;
    }
    
    public final void setId(long p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSummary() {
        return null;
    }
    
    public final void setSummary(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTitle() {
        return null;
    }
    
    public final void setTitle(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getPlatform() {
        return null;
    }
    
    public final void setPlatform(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getMemo() {
        return null;
    }
    
    public final void setMemo(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOriginUrl() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getThumbnailImageUrl() {
        return null;
    }
    
    public final void setThumbnailImageUrl(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    public final boolean isMarked() {
        return false;
    }
    
    public final void setMarked(boolean p0) {
    }
    
    public final boolean isChecked() {
        return false;
    }
    
    public final void setChecked(boolean p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.jordyma.blink.feed.domain.Status getStatus() {
        return null;
    }
    
    public final void setStatus(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.feed.domain.Status p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.jordyma.blink.folder.Folder getFolder() {
        return null;
    }
    
    public final void setFolder(@org.jetbrains.annotations.Nullable()
    com.jordyma.blink.folder.Folder p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.jordyma.blink.keyword.Keyword> getKeywords() {
        return null;
    }
    
    public final void setKeywords(@org.jetbrains.annotations.NotNull()
    java.util.List<com.jordyma.blink.keyword.Keyword> p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.jordyma.blink.recommend.Recommend> getRecommendFolders() {
        return null;
    }
    
    public final void setRecommendFolders(@org.jetbrains.annotations.NotNull()
    java.util.List<com.jordyma.blink.recommend.Recommend> p0) {
    }
    
    public final void updateIsMarked(boolean newIsMarked) {
    }
    
    public final void updateKeywords(@org.jetbrains.annotations.NotNull()
    java.util.List<com.jordyma.blink.keyword.Keyword> keywords) {
    }
    
    public final void updateMemo(@org.jetbrains.annotations.NotNull()
    java.lang.String memo) {
    }
    
    public final void updateStatus(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.feed.domain.Status status) {
    }
    
    public final void updateIsChecked() {
    }
    
    public final void updateThumbnailImageUrl(@org.jetbrains.annotations.NotNull()
    java.lang.String imageUrl) {
    }
    
    public final void updateFolder(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.folder.Folder folder) {
    }
    
    public final void update(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String summary, @org.jetbrains.annotations.NotNull()
    java.lang.String memo, @org.jetbrains.annotations.NotNull()
    com.jordyma.blink.folder.Folder folder) {
    }
    
    public final void update(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.folder.Folder folder, @org.jetbrains.annotations.NotNull()
    com.jordyma.blink.feed.domain.Status status) {
    }
    
    public final void updateSummarizedContent(@org.jetbrains.annotations.NotNull()
    java.lang.String summary, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    com.jordyma.blink.feed.domain.Source brunch) {
    }
}