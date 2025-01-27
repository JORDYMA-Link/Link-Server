package com.jordyma.blink.keyword

import com.jordyma.blink.common.BaseTimeEntity
import com.jordyma.blink.feed.Feed
import jakarta.persistence.*

@Entity
class Keyword(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    val feed: Feed,

    @Column(name = "content", length = 20)
    val content: String,
): BaseTimeEntity()