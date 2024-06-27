package com.jordyma.blink.user.entity

import com.jordyma.blink.global.entity.BaseTimeEntity
import jakarta.persistence.*
import lombok.Getter
import java.time.LocalDateTime


@Getter
@Entity(name = "Member")
class User: BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null

    @Column(name = "nickname")
    var nickname: String? = null

    @Column(name = "social_type")
    @Enumerated(EnumType.STRING)
    var socialType: SocialType? = null

    @Column(name = "social_user_id")
    var socialUserId: String? = null

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    var role: Role? = null

    companion object {
        fun of(nickname: String, socialType: SocialType, socialUserId: String, role: Role): User {
            val user = User()
            user.nickname = nickname
            user.socialType = socialType
            user.socialUserId = socialUserId
            user.role = role
            return user
        }
    }

}