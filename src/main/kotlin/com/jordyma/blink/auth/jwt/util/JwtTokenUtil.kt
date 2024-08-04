package com.jordyma.blink.auth.jwt.util

import com.jordyma.blink.user.entity.User
import com.jordyma.blink.global.http.api.KakaoAuthApi
import com.jordyma.blink.auth.jwt.enums.TokenType
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.global.http.response.OpenKeyListResponse
import io.jsonwebtoken.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.security.Key
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.SignatureException
import java.security.spec.InvalidKeySpecException
import java.security.spec.RSAPublicKeySpec
import java.util.*


@Component
class JwtTokenUtil(private val kakaoAuthApi: KakaoAuthApi) {

    @Value("\${jwt.secret}")
    private val jwtSecret: String? = null

    private val AUTHORIZATION_HEADER: String = "Authorization"

    private val BEARER_PREFIX: String = "Bearer "

    //TODO 환경 변수로 빼기
    private val ACCESS_TOKEN_EXPIRATION_MS: Int = 3 * 60 * 1000 /* 24 * 60 * 60 * 1000 */

    private val REFRESH_TOKEN_EXPIRATION_MS: Int = 5 * 60 * 1000 /* 14 * 24 * 60 * 60 * 1000 */

    // jwt 토큰 생성
    fun generateToken(tokenType: TokenType, user: User): String {
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

    fun parseJwt(jwt: String): Jwt<Header<*>, Claims> {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJwt(jwt)
        } catch (e: ExpiredJwtException) {
            throw ApplicationException(ErrorCode.TOKEN_VERIFICATION_EXCEPTION, "토큰이 만료되었습니다.", e)
        } catch (e: MalformedJwtException) {
            throw ApplicationException(ErrorCode.TOKEN_VERIFICATION_EXCEPTION, "올바르지 않은 token입니다.", e)
        }
    }

    fun verifySignature(idToken: String?, kid: String?, aud: String?, iss: String?, nonce: String?) {
        val keyListResponse: OpenKeyListResponse = kakaoAuthApi.getKakaoOpenKeyAddress()


        //TODO openKey 값을 캐싱해서 사용할 수 있도록 수정
        val openKey: OpenKeyListResponse.JWK? = keyListResponse.keys?.stream()
            ?.filter { key -> key.kid.equals(kid) }
            ?.findFirst()
            ?.get()
//            .orElseThrow {
//                ApplicationException(ErrorCode.OPENKEY_NOT_MATCHED)
//            }

        try {
            val claims: Jws<Claims> = Jwts.parserBuilder()
                .requireAudience(aud)
                .requireIssuer(iss)
                .setSigningKey(getRSAPublicKey(openKey?.n, openKey?.e))
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

    fun parseToken(token: String?): Jws<Claims>? {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
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

    fun isValidToken(token: String?): Boolean {
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

    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    private fun getRSAPublicKey(modulus: String?, exponent: String?): Key {
        val keyFactory: KeyFactory = KeyFactory.getInstance("RSA")
        val decodeN: ByteArray = Base64.getUrlDecoder().decode(modulus)
        val decodeE: ByteArray = Base64.getUrlDecoder().decode(exponent)
        val n = BigInteger(1, decodeN)
        val e = BigInteger(1, decodeE)

        val keySpec = RSAPublicKeySpec(n, e)
        return keyFactory.generatePublic(keySpec)
    }
}