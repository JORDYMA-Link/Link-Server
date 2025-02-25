package com.jordyma.blink.recommend

import com.jordyma.blink.common.BaseTimeEntity
import com.jordyma.blink.feed.domain.Feed
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
