package com.jordyma.blink.folder.entity

import com.jordyma.blink.common.entity.BaseTimeEntity
import com.jordyma.blink.user.entity.User
import jakarta.persistence.*


@Entity
@Table(name = "folder")
class Folder(

    @ManyToOne @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @Column(nullable = false)
    var name: String,

    var count: Int,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

): BaseTimeEntity(){

    companion object {
        fun of(user: User, name: String, count:Int): Folder {
            return Folder(user = user, name = name, count = count)
        }
    }
}
