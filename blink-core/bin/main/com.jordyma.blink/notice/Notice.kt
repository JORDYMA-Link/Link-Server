package com.jordyma.blink.notice

import com.jordyma.blink.common.BaseTimeEntity
import jakarta.persistence.*

@Entity
class Notice(
    @Column(nullable = false, length = 50)
    val title: String,

    @Column(nullable = false, length = 1000)
    val content: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
): BaseTimeEntity()