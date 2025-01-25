package com.jordyma.blink.user_refresh_token.entity

import com.jordyma.blink.user.entity.User
import jakarta.persistence.*

@Entity
class UserRefreshToken protected constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "refresh_token", columnDefinition = "VARCHAR(500)")
    var refreshToken: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = null
) {

    fun updateRefreshToken(newRefreshToken: String?) {
        refreshToken = newRefreshToken
    }

    companion object {
        fun of(refreshToken: String?, user: User?) = UserRefreshToken(
            refreshToken = refreshToken,
            user = user
        )
    }
}