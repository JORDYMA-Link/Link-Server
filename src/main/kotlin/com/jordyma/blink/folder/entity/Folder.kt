package com.jordyma.blink.folder.entity

import com.jordyma.blink.global.entity.BaseTimeEntity
import com.jordyma.blink.user.entity.User
import jakarta.persistence.*

@Entity
class Folder(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long,

    @ManyToOne @JoinColumn(name = "user_id")
    val user: User,

    @Column(name = "name", length = 50)
    val name: String,

    @Column(name = "count")
    val count: Int,
): BaseTimeEntity() {
    fun increaseCount() {
        count++
    }

    fun decreaseCount() {
        count--
    }
}
