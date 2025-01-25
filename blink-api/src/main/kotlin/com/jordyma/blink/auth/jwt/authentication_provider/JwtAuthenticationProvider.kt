package com.jordyma.blink.auth.jwt.authentication_provider

import com.jordyma.blink.auth.jwt.user_account.UserAccountService
import com.jordyma.blink.auth.jwt.util.JwtTokenUtil
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component


@Component
class JwtAuthenticationProvider(
    private val jwtTokenUtil: JwtTokenUtil,
    private val userAccountService: UserAccountService,
    @Value("\${jwt.secret}") private val jwtSecret: String
): AuthenticationProvider {
    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication? {
        if (authentication.principal == null || !jwtTokenUtil.isValidToken(authentication.principal.toString(), jwtSecret)) {
            logger().info("authentication: ${authentication}")
            throw ApplicationException(ErrorCode.TOKEN_VERIFICATION_EXCEPTION, "토큰 verify에 실패하였습니다.")
        }
        val userDetails: UserDetails = userAccountService.loadUserByUsername(authentication.principal as String)
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}