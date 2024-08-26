package com.jordyma.blink.auth.service
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.jordyma.blink.auth.dto.request.AppleLoginRequestDto

import com.jordyma.blink.user.entity.SocialType
import com.jordyma.blink.user.entity.User
import com.jordyma.blink.user.repository.UserRepository
import com.jordyma.blink.auth.dto.request.KakaoLoginRequestDto
import com.jordyma.blink.auth.dto.response.TokenResponseDto
import com.jordyma.blink.auth.jwt.enums.TokenType
import com.jordyma.blink.auth.jwt.util.JwtTokenUtil
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.global.http.api.KakaoAuthApi
import com.jordyma.blink.global.http.response.OpenKeyListResponse
import com.jordyma.blink.user.entity.Role
import com.jordyma.blink.user_refresh_token.entity.UserRefreshToken
import com.jordyma.blink.user_refresh_token.repository.UserRefreshTokenRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import lombok.RequiredArgsConstructor
import org.apache.coyote.BadRequestException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.math.BigInteger
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.security.Key
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.PublicKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.RSAPublicKeySpec
import java.util.*

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class AuthService(
    private val jwtTokenUtil: JwtTokenUtil,
    private val userRepository: UserRepository,
    private val kakaoAuthApi: KakaoAuthApi,
    private val userRefreshTokenRepository: UserRefreshTokenRepository,
    @Value("\${kakao.auth.jwt.aud}")  val aud: String? = null,
    @Value("\${kakao.auth.jwt.iss}") val iss: String? = null,
    @Value("\${kakao.auth.jwt.client-id}") val kakaoClientId: String,
    @Value("\${kakao.auth.jwt.redirect-uri}") val kakaoRedirectUri: String,
    @Value("\${jwt.secret}") val jwtSecret: String,
) {

    @Transactional
    fun kakaoLogin(kakaoLoginRequestDto: KakaoLoginRequestDto): TokenResponseDto {
        val idToken: String = kakaoLoginRequestDto.idToken;

        val header = jwtTokenUtil.getJwtHeader(idToken)

        val kid: String = header.header["kid"].toString()
        val publicKey: Key = getKakaoPublicKey(kid)
        val claims = jwtTokenUtil.parseToken(idToken, publicKey)
        jwtTokenUtil.verifySignature(idToken, publicKey, aud, iss, kakaoLoginRequestDto.nonce)

        val nickname: String = claims.body.get("nickname", String::class.java)
        val socialUserId: String = claims.body.get("sub", String::class.java)

        val user: User = upsertUser(SocialType.KAKAO, socialUserId, nickname)

        val accessToken = jwtTokenUtil.generateToken(TokenType.ACCESS_TOKEN, user, jwtSecret)
        val refreshToken = jwtTokenUtil.generateToken(TokenType.REFRESH_TOKEN, user, jwtSecret)

        userRefreshTokenRepository.save(UserRefreshToken.of(refreshToken, user))

        return TokenResponseDto(accessToken, refreshToken);
    }

    fun getKakaoPublicKey(kid: String): Key {
        // TODO 캐싱 필요
        val keyListResponse: OpenKeyListResponse = kakaoAuthApi.getKakaoOpenKeyAddress()

        val openKey: OpenKeyListResponse.JWK? = keyListResponse.keys?.stream()
            ?.filter { key -> key.kid.equals(kid) }
            ?.findFirst()
            ?.get()

        return getRSAPublicKey(openKey?.n, openKey?.e)
    }

    private fun upsertUser(socialType: SocialType, socialUserId: String, nickname: String): User {
        return userRepository.findBySocialTypeAndSocialUserId(socialType, socialUserId)
            ?: userRepository.save(
                User(
                    nickname = nickname,
                    socialType = SocialType.KAKAO,
                    socialUserId = socialUserId,
                    role = Role.USER
                )
            )
    }

    @Transactional
    fun regenerateToken(token: String): TokenResponseDto {
        val claims = jwtTokenUtil.parseToken(token, jwtSecret);
        val tokenType: TokenType = TokenType.valueOf(claims!!.body["type", String::class.java])

        if (tokenType !== TokenType.REFRESH_TOKEN) {
            throw ApplicationException(ErrorCode.TOKEN_VERIFICATION_EXCEPTION, "올바른 토큰 타입이 아닙니다.")
        }

        val userRefreshToken: UserRefreshToken = userRefreshTokenRepository.findByRefreshToken(token)
            ?: throw ApplicationException(ErrorCode.TOKEN_VERIFICATION_EXCEPTION, "올바르지 않은 토큰입니다.")
        val subject = claims.body.subject
        val user: User = userRepository.findById(subject.toLong())
            .orElseThrow { ApplicationException(ErrorCode.TOKEN_VERIFICATION_EXCEPTION, "올바르지 않은 토큰입니다.") }
        val accessToken = jwtTokenUtil.generateToken(TokenType.ACCESS_TOKEN, user, jwtSecret)
        val refreshToken = jwtTokenUtil.generateToken(TokenType.REFRESH_TOKEN, user, jwtSecret)

        userRefreshToken.updateRefreshToken(refreshToken)

        return TokenResponseDto(accessToken, refreshToken)
    }

    @Transactional
    fun kakaoLoginWeb(code: String): TokenResponseDto {

        val tokenResponse = kakaoAuthApi.getKakaoToken(
            client_id = this.kakaoClientId,
            redirect_uri = this.kakaoRedirectUri,
            code = code
        )

        val idToken = tokenResponse.id_token
        val header = jwtTokenUtil.getJwtHeader(idToken);

        val kid: String = header.header["kid"].toString()
        val publicKey: Key = getKakaoPublicKey(kid)
        val claims = jwtTokenUtil.parseToken(idToken, publicKey)

        val nickname: String = claims.body.get("nickname", String::class.java)
        val socialUserId: String = claims.body.get("sub", String::class.java)

        val user: User = upsertUser(SocialType.KAKAO, socialUserId, nickname)

        val accessToken = jwtTokenUtil.generateToken(TokenType.ACCESS_TOKEN, user, jwtSecret)
        val refreshToken = jwtTokenUtil.generateToken(TokenType.REFRESH_TOKEN, user, jwtSecret)

        userRefreshTokenRepository.save(UserRefreshToken.of(refreshToken, user))

        return TokenResponseDto(accessToken, refreshToken)
    }

    @Transactional
    fun appleLogin(appleLoginRequestDto: AppleLoginRequestDto): TokenResponseDto? {

        val decodeArray = appleLoginRequestDto.idToken.split(".")
        val header = String(Base64.getDecoder().decode(decodeArray[0]))

        val headerJson = JsonParser.parseString(header).asJsonObject
        val kid = headerJson["kid"]
        val alg = headerJson["alg"]

        val publicKey: PublicKey = this.getPublicKey(kid, alg)
        val userInfo: Claims = Jwts.parserBuilder().setSigningKey(publicKey)
            .build().parseClaimsJws(appleLoginRequestDto.idToken).body


        val objectMapper = ObjectMapper()
        val jsonString = objectMapper.writeValueAsString(userInfo)

        val userInfoObject = JsonParser.parseString(jsonString).asJsonObject

        val email = userInfoObject["email"].asString
        val socialUserId = userInfoObject["sub"].asString
        val name = if (userInfoObject.has("name")) userInfoObject["name"].asString else ""

        // 첫 가입인 경우
        val findUser = userRepository.findBySocialTypeAndSocialUserId(SocialType.APPLE, socialUserId)

        if (findUser == null) {
            val socialId = userInfo["sub"] as String

            val user: User = upsertUser(SocialType.APPLE, socialUserId, name)
            userRepository.save(user)

            val accessToken = jwtTokenUtil.generateToken(TokenType.ACCESS_TOKEN, user, jwtSecret)
            val refreshToken = jwtTokenUtil.generateToken(TokenType.REFRESH_TOKEN, user, jwtSecret)
            userRefreshTokenRepository.save(UserRefreshToken.of(refreshToken, user))

            return TokenResponseDto(accessToken, refreshToken);
        }

        // 이미 가입한 경우
        val socialId = userInfo["sub"] as String

        val requestUser = userRepository.findBySocialTypeAndSocialUserId(SocialType.APPLE, socialUserId)
        //  .orElseThrow { throw Exception("유저를 찾을 수 없습니다.") }

        return regenerateToken(appleLoginRequestDto.idToken)
    }

    fun getPublicKey(kid: JsonElement, alg: JsonElement): PublicKey {
        val keys: JsonArray = getApplePublicKeys()

        var availableObject: JsonObject? = null

        for (i in 0 until keys.size()) {
            val appleObject = keys.get(i).asJsonObject
            val appleKid = appleObject["kid"]
            val appleAlg = appleObject["alg"]

            if (appleKid == kid && appleAlg == alg) {
                availableObject = appleObject
                break
            }
        }

        // 일치하는 공개키 없음
        if (availableObject == null) {
            throw BadRequestException("유효하지 않은 토큰입니다.")
        }

        val nStr = availableObject["n"].asString
        val eStr = availableObject["e"].asString

        val nBytes = Base64.getUrlDecoder().decode(nStr)
        val eBytes = Base64.getUrlDecoder().decode(eStr)

        val n = BigInteger(1, nBytes)
        val e = BigInteger(1, eBytes)

        return try {
            val publicKeySpec = RSAPublicKeySpec(n, e)
            val keyFactory = KeyFactory.getInstance("RSA")
            keyFactory.generatePublic(publicKeySpec)

        } catch (exception: Exception) {
            throw Exception("애플 로그인 퍼블릭 키를 불러오는데 실패했습니다.")
        }
    }

    fun getApplePublicKeys(): JsonArray {
        val apiKey = StringBuilder()

        try {
            val url = URL("https://appleid.apple.com/auth/keys")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"

            BufferedReader(InputStreamReader(conn.inputStream)).use { br ->
                var line: String?
                while (br.readLine().also { line = it } != null) {
                    apiKey.append(line)
                }
            }
            val keys = JsonParser.parseString(apiKey.toString()).asJsonObject
            return keys["keys"].asJsonArray

        } catch (e: IOException) {
            throw Exception("URL 파싱 실패")
        }
    }

    private fun getRSAPublicKey(modulus: String?, exponent: String?): Key {
        val keyFactory: KeyFactory = KeyFactory.getInstance("RSA")
        val decodeN: ByteArray = Base64.getUrlDecoder().decode(modulus)
        val decodeE: ByteArray = Base64.getUrlDecoder().decode(exponent)
        val n = BigInteger(1, decodeN)
        val e = BigInteger(1, decodeE)

        val keySpec = RSAPublicKeySpec(n, e)
        return kotlin.runCatching {
            keyFactory.generatePublic(keySpec)
        } .getOrElse {
            exception -> throw ApplicationException(ErrorCode.TOKEN_VERIFICATION_EXCEPTION, "토큰 인증에 실패하였습니다.", exception)
        }
    }
}