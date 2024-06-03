package com.jordyma.blink.user_refresh_token.entity

import com.jordyma.blink.User.entity.User
import jakarta.persistence.*
import lombok.AccessLevel
import lombok.Getter
import lombok.NoArgsConstructor

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UserRefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private var id: Long? = null

    @Column(name = "refresh_token", columnDefinition = "VARCHAR(500)")
    private var refreshToken: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private var user: User? = null

    fun updateRefreshToken(refreshToken: String?) {
        this.refreshToken = refreshToken
    }

    companion object {
        fun of(refreshToken: String?, user: User?): UserRefreshToken {
            val userRefreshToken = UserRefreshToken()
            userRefreshToken.refreshToken = refreshToken
            userRefreshToken.user = user

            return userRefreshToken
        }
    }
}