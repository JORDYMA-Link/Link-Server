package com.jordyma.blink.user.entity

import com.jordyma.blink.global.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity(name = "user")
class User(
    @Column(name = "nickname")
    var nickname: String = "",

    @Column(name = "socialType")
    @Enumerated(EnumType.STRING)
    val socialType: SocialType? = null,

    @Column(name = "socialUserId")
    val socialUserId: String? = null,

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    var role: Role? = null

): BaseTimeEntity()  {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null
}