package com.jordyma.blink.feed.entity

import com.jordyma.blink.global.entity.BaseTimeEntity
import com.jordyma.blink.folder.entity.Folder
import com.jordyma.blink.keyword.entity.Keyword
import jakarta.persistence.*

@Entity
class Feed(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long,

    @Column(name = "summary", length = 200)
    val summary: String,

    @Column(name = "title", length = 100)
    val title: String,

    @Column(name = "source", length = 100)
    val source: String,

    @Column(name = "source_url", length = 255)
    val sourceUrl: String,

    @Column(name = "memo", columnDefinition = "TEXT")
    var memo: String = "",

    @Column(name = "thumbnail_image", length = 255)
    val thumbnailImage: String,

    @Column(name = "url", length = 255)
    val url: String,

    @Column(name = "is_marked", columnDefinition = "BIT")
    val isMarked: Boolean,

    @ManyToOne @JoinColumn(name = "folder_id")
    val folder: Folder,

    @OneToMany(mappedBy = "feed")
    val keywords: List<Keyword>,
): BaseTimeEntity()
