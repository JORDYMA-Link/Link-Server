package com.jordyma.blink.user_refresh_token.repository

import com.jordyma.blink.user_refresh_token.entity.UserRefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRefreshTokenRepository : JpaRepository<UserRefreshToken, Long> {
    fun findByRefreshToken(refreshToken: String?): UserRefreshToken?

    @Query("select urt from UserRefreshToken urt where urt.user.id =:id")
    fun findByUserId(id: Long): List<UserRefreshToken>
}