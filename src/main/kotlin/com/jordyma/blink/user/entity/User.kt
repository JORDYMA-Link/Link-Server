package com.jordyma.blink.user.entity

import com.jordyma.blink.global.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity(name = "Member")
class User(
    @Column(name = "nickname")
    var nickname: String = "",

    @Column(name = "social_type")
    @Enumerated(EnumType.STRING)
    val socialType: SocialType? = null,

    @Column(name = "social_user_id")
    val socialUserId: String? = null,

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    var role: Role? = null

): BaseTimeEntity()  {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null
}
