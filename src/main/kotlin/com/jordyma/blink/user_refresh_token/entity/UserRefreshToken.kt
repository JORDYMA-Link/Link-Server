package com.jordyma.blink.user_refresh_token.entity

import com.jordyma.blink.global.entity.BaseTimeEntity
import com.jordyma.blink.user.entity.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class UserRefreshToken(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "refresh_token", columnDefinition = "VARCHAR(500)")
    var refreshToken: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = null,

    @Column(name = "token_expiration_time", columnDefinition = "DATETIME")
    var tokenExpirationTime: LocalDateTime? = null,

): BaseTimeEntity(){

    fun updateRefreshToken(refreshToken: String?) {
        this.refreshToken = refreshToken
    }

    fun expire(now: LocalDateTime) {
        if (tokenExpirationTime!!.isAfter(now)) {
            this.tokenExpirationTime = now
        }
    }

    companion object {
        fun of(refreshToken: String?, user: User?, expirationTime: LocalDateTime): UserRefreshToken {
            val userRefreshToken = UserRefreshToken()
            userRefreshToken.refreshToken = refreshToken
            userRefreshToken.user = user
            userRefreshToken.tokenExpirationTime = expirationTime

            return userRefreshToken
        }
    }
}