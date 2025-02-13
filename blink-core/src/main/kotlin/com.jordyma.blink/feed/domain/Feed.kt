package com.jordyma.blink.feed.domain

import com.jordyma.blink.common.BaseTimeEntity
import com.jordyma.blink.folder.Folder
import com.jordyma.blink.keyword.Keyword
import com.jordyma.blink.recommend.Recommend
import jakarta.persistence.*

@Entity
@Table(name = "feed")
class Feed(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(name = "summary", length = 200)
    var summary: String,

    @Column(name = "title", length = 100)
    var title: String,

    @Column(name = "platform", length = 100)
    var platform: String? = "",

    @Column(name = "memo", columnDefinition = "TEXT")
    var memo: String? = "",

    @Column(name = "origin_url", length = 512)
    val originUrl: String,

    @Column(name = "thumbnail_image_url", length = 200)
    var thumbnailImageUrl: String? = "",

    @Column(name = "is_marked", columnDefinition = "BIT")
    var isMarked: Boolean = false,

    @Column(name = "is_checked", columnDefinition = "BIT")
    var isChecked: Boolean = false,

    @Column(name = "status", length = 10)
    @Enumerated(EnumType.STRING)
    var status: Status = Status.REQUESTED,

    @ManyToOne(cascade = [CascadeType.PERSIST], fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    var folder: Folder? = null,

    @OneToMany(mappedBy = "feed")
    var keywords: List<Keyword> = emptyList(),

    @OneToMany(mappedBy = "feed")
    var recommendFolders: List<Recommend> = emptyList(),
): BaseTimeEntity(){

    fun updateIsMarked(newIsMarked: Boolean){
        this.isMarked = newIsMarked
    }

    fun updateKeywords(
        keywords: List<Keyword>
    ) {
        this.keywords = keywords
    }

    fun updateMemo(memo: String){
        this.memo = memo
    }

    fun updateStatus(status: Status){
        this.status = status
    }

    fun updateIsChecked(){
        this.isChecked = true
    }

    fun updateThumbnailImageUrl(imageUrl: String){
        this.thumbnailImageUrl = imageUrl
    }

    fun updateFolder(folder: Folder){
        this.folder = folder
    }

    fun update(title: String,
               summary: String,
               memo: String,
               folder: Folder){
        this.title = title
        this.summary = summary
        this.memo = memo
        this.folder = folder
    }

    fun updateSummarizedContent(summary: String, title: String, brunch: Source) {
        this.summary = summary
        this.title = title
        this.platform = brunch.source
        this.status = Status.COMPLETED
    }
}