package com.jordyma.blink.User

import jakarta.persistence.*
import lombok.Getter
import java.time.LocalDateTime


@Getter
@Entity
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null;

    @Column(name = "nickname")
    val nickname: String? = null;

    @Column(name = "social_type")
    @Enumerated(EnumType.STRING)
    val socialType: SocialType? = null;

    @Column(name = "social_user_id")
    val socialUserId: String? = null

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    val role: Role? = null

    @Column(name = "created_at")
    val createdAt: LocalDateTime? = null;

    @Column(name = "updated_at")
    val updatedAt: LocalDateTime? = null;


    @Column(name = "deleted_at")
    val deletedAt: LocalDateTime? = null;

}