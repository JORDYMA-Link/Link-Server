package com.jordyma.blink.folder.entity

import com.jordyma.blink.global.entity.BaseTimeEntity
import com.jordyma.blink.user.entity.User
import jakarta.persistence.*

@Entity
class Folder(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val user: User,

    @Column(length = 50)
    var name: String,

    @Column(nullable = false)
    var count: Int,
): BaseTimeEntity() {
    fun increaseCount() {
        count++
    }

    fun decreaseCount() {
        count--
    }
}