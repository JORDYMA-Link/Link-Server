package com.jordyma.blink.feed.entity

import com.jordyma.blink.global.entity.BaseTimeEntity
import com.jordyma.blink.folder.entity.Folder
import jakarta.persistence.*

@Entity
class Feed(
    @Column(length = 200)
    val summary: String,

    @Column(length = 100)
    val title: String,

    @Column(length = 100)
    val platform: String,

    @Column(length = 255)
    val sourceUrl: String,

    @Column(columnDefinition = "TEXT")
    var memo: String = "",

    @Column(length = 255)
    val thumbnailImage: String,

    @Column(length = 255)
    val url: String,

    @ManyToOne @JoinColumn(name = "folder_id")
    val folder: Folder,
): BaseTimeEntity() {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null

    @Column(columnDefinition = "BIT")
    var isMarked: Boolean = false
        private set

    fun changeIsMarked(newIsMarked: Boolean){
        this.isMarked = newIsMarked
    }
}
