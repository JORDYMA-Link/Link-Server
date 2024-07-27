package com.jordyma.blink.feed.entity

import com.jordyma.blink.global.entity.BaseTimeEntity
import com.jordyma.blink.folder.entity.Folder
import com.jordyma.blink.keyword.entity.Keyword
import jakarta.persistence.*

@Entity
class Feed(
    @Column(name = "summary", length = 200)
    val summary: String,

    @Column(name = "title", length = 100)
    val title: String,

    @Column(name = "platform", length = 100)
    val platform: String,

    @Column(name = "platform_image", length = 255)
    val platformImage: String,

    @Column(name = "memo", columnDefinition = "TEXT")
    var memo: String = "",

    @Column(name = "thumbnail_image", length = 255)
    val thumbnailImage: String,

    @Column(name = "origin_url", length = 255)
    val originUrl: String,

    @ManyToOne @JoinColumn(name = "folder_id")
    val folder: Folder,

    @OneToMany(mappedBy = "feed")
    val keywords: List<Keyword>,
): BaseTimeEntity() {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null

    @Column(name = "is_marked", columnDefinition = "BIT")
    var isMarked: Boolean = false
        private set

    fun changeIsMarked(newIsMarked: Boolean){
        this.isMarked = newIsMarked
    }
}
