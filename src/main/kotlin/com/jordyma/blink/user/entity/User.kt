package com.jordyma.blink.user.entity

import com.jordyma.blink.global.entity.BaseTimeEntity
import jakarta.persistence.*
import lombok.Getter
import java.time.LocalDateTime


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

    @Column(name = "created_at")
    var createdAt: LocalDateTime? = null;

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null;

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null;

    companion object {
        fun of(nickname: String, socialType: SocialType, socialUserId: String, role: Role): User {
            val user = User()
            user.nickname = nickname
            user.socialType = socialType
            user.socialUserId = socialUserId
            user.role = role
            user.createdAt = LocalDateTime.now()
            user.updatedAt = LocalDateTime.now()
            return user
        }
    }
): BaseTimeEntity()  {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null
}
