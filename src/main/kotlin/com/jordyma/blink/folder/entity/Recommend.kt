package com.jordyma.blink.folder.entity

import com.jordyma.blink.global.entity.BaseTimeEntity
import com.jordyma.blink.user.entity.User
import jakarta.persistence.*

@Entity
class Recommend (

    @ManyToOne @JoinColumn(name = "folder_id")
    val folder: Folder,

    @Column(name = "priority", nullable = false)
    val priority: Int,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

): BaseTimeEntity()
