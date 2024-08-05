package com.jordyma.blink.feed.entity

import com.jordyma.blink.global.entity.BaseTimeEntity
import com.jordyma.blink.folder.entity.Folder
import com.jordyma.blink.folder.entity.Recommend
import com.jordyma.blink.image.entity.thumbnail.ThumbnailImage
import com.jordyma.blink.keyword.entity.Keyword
import jakarta.persistence.*

@Entity
class Feed(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "summary", length = 200)
    var summary: String,

    @Column(name = "title", length = 100)
    var title: String,

    @Column(name = "source", length = 100)
    val source: String? = "",

//    @Column(name = "source_url", length = 255)
//    val sourceUrl: String? = "",

    @Column(name = "memo", columnDefinition = "TEXT")
    var memo: String? = "",

    @OneToOne @PrimaryKeyJoinColumn(name = "thumbnail_image_id")
    var thumbnailImage: ThumbnailImage? = null,

    @Column(name = "url", length = 255)
    var url: String,

    @Column(name = "is_marked", columnDefinition = "BIT")
    var isMarked: Boolean = false,

    @ManyToOne @JoinColumn(name = "folder_id")
    var folder: Folder,

    @OneToMany(mappedBy = "feed")
    var keywords: List<Keyword>? = emptyList(),

    @OneToMany(mappedBy = "feed")
    var recommendFolders: List<Recommend>? = emptyList(),
): BaseTimeEntity(){
    fun updateKeywords(
        keywords: List<Keyword>
    ) {
        this.keywords = keywords
    }

    fun updateRecommendFolders(
        recommendFolders: List<Recommend>
    ) {
        this.recommendFolders = recommendFolders
    }
}
