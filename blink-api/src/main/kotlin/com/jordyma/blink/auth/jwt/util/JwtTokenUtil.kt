package com.jordyma.blink.auth.jwt.util

import com.jordyma.blink.user.User
import com.jordyma.blink.auth.jwt.enums.TokenType
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import io.jsonwebtoken.*
import org.springframework.stereotype.Component
import java.security.Key
import java.security.NoSuchAlgorithmException
import java.security.SignatureException
import java.security.spec.InvalidKeySpecException
import java.util.*

@Component
class JwtTokenUtil() {

    //TODO 환경 변수로 빼기
    private val ACCESS_TOKEN_EXPIRATION_MS: Int = 3 * 60 * 60 * 1000

    private val REFRESH_TOKEN_EXPIRATION_MS: Int = 14 * 24 * 60 * 60 * 1000

    // jwt 토큰 생성
    fun generateToken(tokenType: TokenType, user: User, jwtSecret: String): String {
        val now: Date = Date()

        val expireDuration =
            if (tokenType === TokenType.REFRESH_TOKEN) REFRESH_TOKEN_EXPIRATION_MS else ACCESS_TOKEN_EXPIRATION_MS

        val expiryDate: Date = Date(now.getTime() + expireDuration)
        val claims: Claims = Jwts.claims()
            .setSubject(user.id.toString()) // 사용자
            .setIssuedAt(Date()) // 현재 시간 기반으로 생성
            .setExpiration(expiryDate)
        // 만료 시간 세팅

        claims.put("user_id", user.id)
        claims.put("nick_name", user.nickname)
        claims.put("type", tokenType)
        claims.put("role", user.role)
        val token: String = Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS512, jwtSecret) // 사용할 암호화 알고리즘, signature에 들어갈 secret 값 세팅
            .compact()

        return token
    }

    private fun removeSignature(jwt: String): String {
        val jwtSplit = jwt.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (jwtSplit.size != 3) {
            throw ApplicationException(ErrorCode.TOKEN_VERIFICATION_EXCEPTION, "토큰 인증에 실패하였습니다.")
        }
        return jwtSplit[0] + "." + jwtSplit[1] + "."
    }

    fun getJwtHeader(jwt: String): Jwt<Header<*>, Claims> {
        val jwtWithoutSignature = removeSignature(jwt)

        return kotlin.runCatching { Jwts.parserBuilder()
            .build()
            .parseClaimsJwt(jwtWithoutSignature)
            } .getOrElse {
            throw ApplicationException(ErrorCode.TOKEN_EXPIRED, "토큰이 만료되었습니다.")
        }
    }

    fun verifySignature(idToken: String?, key: Key, aud: String?, iss: String?, nonce: String?) {
        try {
            val claims: Jws<Claims> = Jwts.parserBuilder()
                .requireAudience(aud)
                .requireIssuer(iss)
                .setSigningKey(key)
                .build()
                .parseClaimsJws(idToken)

            if (nonce != claims.body.get("nonce", String::class.java)) {
                throw ApplicationException(ErrorCode.NONCE_NOT_MATCHED, "nonce가 일치하지 않습니다.")
            }
        } catch (e: NoSuchAlgorithmException) {
            throw ApplicationException(ErrorCode.TOKEN_VERIFICATION_EXCEPTION, "토큰 인증에 실패하였습니다.")
        } catch (e: InvalidKeySpecException) {
            throw ApplicationException(ErrorCode.TOKEN_VERIFICATION_EXCEPTION, "토큰 인증에 실패하였습니다.")
        }
    }

    fun parseToken(token: String, secret: String): Jws<Claims>? {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
        } catch (e: ExpiredJwtException) {
            throw ApplicationException(ErrorCode.TOKEN_VERIFICATION_EXCEPTION, "토큰 인증에 실패하였습니다.")
        } catch (e: MalformedJwtException) {
            throw ApplicationException(ErrorCode.TOKEN_VERIFICATION_EXCEPTION, "토큰 인증에 실패하였습니다.")
        } catch (e: SignatureException) {
            throw ApplicationException(ErrorCode.TOKEN_VERIFICATION_EXCEPTION, "토큰 인증에 실패하였습니다.")
        }
    }

    fun parseToken(token: String, secret: Key): Jws<Claims> {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
        } catch (e: ExpiredJwtException) {
            throw ApplicationException(ErrorCode.TOKEN_VERIFICATION_EXCEPTION, "토큰 인증에 실패하였습니다.")
        } catch (e: MalformedJwtException) {
            throw ApplicationException(ErrorCode.TOKEN_VERIFICATION_EXCEPTION, "토큰 인증에 실패하였습니다.")
        } catch (e: SignatureException) {
            throw ApplicationException(ErrorCode.TOKEN_VERIFICATION_EXCEPTION, "토큰 인증에 실패하였습니다.")
        }
    }

    fun isValidToken(token: String, jwtSecret: String): Boolean {
        try {
            Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)

            return true
        } catch (e: ExpiredJwtException) {
            return false
        } catch (e: MalformedJwtException) {
            return false
        } catch (e: SignatureException) {
            return false
        }
    }
}