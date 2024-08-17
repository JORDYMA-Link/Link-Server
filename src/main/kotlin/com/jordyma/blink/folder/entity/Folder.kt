package com.jordyma.blink.folder.entity

import com.jordyma.blink.global.entity.BaseTimeEntity
import com.jordyma.blink.user.entity.User
import jakarta.persistence.*

@Entity
class Folder(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @ManyToOne @JoinColumn(name = "user_id")
    val user: User,

    @Column(name = "name", length = 50)
    var name: String,

    @Column(name = "count")
    var count: Int,

    @Column(name = "is_unclassified")
    var isUnclassified: Boolean,
): BaseTimeEntity() {

    fun increaseCount() {
        count++
    }

    fun decreaseCount() {
        count--
    }
}
