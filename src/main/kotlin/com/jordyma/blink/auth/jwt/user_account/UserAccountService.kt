package com.jordyma.blink.auth.jwt.user_account

import com.jordyma.blink.User.entity.Role
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class UserAccountService: UserDetailsService {
    @Value("\${jwt.secret}")
    private val jwtSecret: String? = null

    override fun loadUserByUsername(token: String?): UserDetails {
        val claims = Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token)
        val userId = claims.body.get("user_id", Long::class.java)
        val profileUrl = claims.body.get("profile_url", String::class.java)
        val nickName = claims.body.get("nick_name", String::class.java)
        val role: Role = Role.valueOf(claims.body["role", String::class.java])

        return UserAccount(userId, profileUrl, nickName, role)
    }
}