package com.jordyma.blink.keyword.entity

import com.jordyma.blink.global.entity.BaseTimeEntity
import com.jordyma.blink.feed.entity.Feed
import jakarta.persistence.*

@Entity
class Keyword(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    val feed: Feed,

    @Column(name = "keyword", length = 20)
    val keyword: String,
): BaseTimeEntity()