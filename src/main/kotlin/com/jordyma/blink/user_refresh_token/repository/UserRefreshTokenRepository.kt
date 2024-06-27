package com.jordyma.blink.user_refresh_token.repository

import com.jordyma.blink.user_refresh_token.entity.UserRefreshToken
import org.springframework.data.jpa.repository.JpaRepository

interface UserRefreshTokenRepository : JpaRepository<UserRefreshToken?, Long?> {
    fun findByRefreshToken(refreshToken: String?): UserRefreshToken?
}