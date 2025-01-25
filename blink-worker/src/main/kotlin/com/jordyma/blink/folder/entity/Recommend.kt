package com.jordyma.blink.folder.entity

import com.jordyma.blink.feed.entity.Feed
import com.jordyma.blink.global.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
class Recommend (

    @ManyToOne @JoinColumn(name = "feed_id")
    var feed: Feed,

    @Column(name = "folder_name", nullable = false)
    val folderName: String,

    @Column(name = "priority", nullable = false)
    val priority: Int,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    ): BaseTimeEntity()
