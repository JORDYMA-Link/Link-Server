package com.jordyma.blink.feed.entity

import com.jordyma.blink.global.entity.BaseTimeEntity
import com.jordyma.blink.folder.entity.Folder
import jakarta.persistence.*

@Entity
class Feed(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Column(length = 200)
    val summary: String,
    @Column(length = 100)
    val title: String,
    val source: String,
    val sourceUrl: String,
    @Column(length = 200)
    var memo: String = "",
    val thumbnailImage: String,
    val url: String,
    @Column(columnDefinition = "BIT")
    val isMarked: Boolean,
    @ManyToOne @JoinColumn(name = "folder_id")
    val folder: Folder,
): BaseTimeEntity()
