package com.jordyma.blink.folder.entity

import com.jordyma.blink.global.entity.BaseTimeEntity
import com.jordyma.blink.user.entity.User
import jakarta.persistence.*

@Entity
class Folder(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @ManyToOne @JoinColumn(name = "user_id")
    val user: User,

    @Column(length = 50)
    val name: String,

    val count: Int,
): BaseTimeEntity()