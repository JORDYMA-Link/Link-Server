package com.jordyma.blink.auth.service
import com.amazonaws.services.s3.AmazonS3
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.jordyma.blink.auth.dto.request.AppleLoginRequestDto
import com.jordyma.blink.user.SocialType
import com.jordyma.blink.user.User
import com.jordyma.blink.user.UserRepository
import com.jordyma.blink.auth.dto.request.KakaoLoginRequestDto
import com.jordyma.blink.auth.dto.response.AppleDto
import com.jordyma.blink.auth.dto.response.TokenResponseDto
import com.jordyma.blink.auth.jwt.enums.TokenType
import com.jordyma.blink.auth.jwt.util.JwtTokenUtil
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.global.http.api.KakaoAuthApi
import com.jordyma.blink.global.http.response.OpenKeyListResponse
import com.jordyma.blink.auth.jwt.user_account.UserAccount
import com.jordyma.blink.feed.domain.Feed
import com.jordyma.blink.feed.domain.Source
import com.jordyma.blink.feed.domain.Status
import com.jordyma.blink.feed.domain.FeedRepository
import com.jordyma.blink.folder.Folder
import com.jordyma.blink.folder.FolderRepository
import com.jordyma.blink.keyword.Keyword
import com.jordyma.blink.keyword.KeywordRepository
import com.jordyma.blink.logger
import com.jordyma.blink.user.Role
import com.jordyma.blink.user.UserRefreshToken
import com.jordyma.blink.user.UserRefreshTokenRepository
import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.ECDSASigner
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import com.nimbusds.oauth2.sdk.TokenResponse
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import lombok.RequiredArgsConstructor
import net.minidev.json.JSONObject
import net.minidev.json.parser.JSONParser
import org.apache.coyote.BadRequestException
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.io.*
import java.math.BigInteger
import java.net.HttpURLConnection
import java.net.URL
import java.security.InvalidKeyException
import java.security.Key
import java.security.PublicKey
import java.security.interfaces.ECPrivateKey
import java.security.spec.RSAPublicKeySpec
import java.time.Instant
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class AuthService(
    private val jwtTokenUtil: JwtTokenUtil,
    private val userRepository: UserRepository,
    private val folderRepository: FolderRepository,
    private val feedRepository: FeedRepository,
    private val keywordRepository: KeywordRepository,
    private val kakaoAuthApi: KakaoAuthApi,
    private val userRefreshTokenRepository: UserRefreshTokenRepository,
    private val restTemplate: RestTemplate,
    private val amazonS3: AmazonS3,
    @Value("\${apple.team-id}") private val appleTeamId: String,
    @Value("\${apple.login-key}") private val appleLoginKey: String,
    @Value("\${apple.redirect-url}") private val appleWebRedirectUrl: String,
    @Value("\${kakao.auth.jwt.aud}")  val aud: String? = null,
    @Value("\${kakao.auth.jwt.iss}") val iss: String? = null,
    @Value("\${kakao.auth.jwt.client-id}") val kakaoClientId: String,
    @Value("\${kakao.auth.jwt.redirect-uri}") val kakaoRedirectUri: String,
    @Value("\${jwt.secret}") val jwtSecret: String,
    @Value("\${apple.team-id}") val teamId: String? = null,
    @Value("\${apple.login-key}") val loginKey: String? = null,
    @Value("\${apple.client-id}") private val appleClientId: String,
    @Value("\${apple.web-client-id}") val appleWebClientId: String? = null,
    @Value("\${apple.key-path}") val keyPath: String? = null,
    @Value("\${spring.cloud.aws.s3.bucket}") private val bucket: String,
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
        if(folderRepository.findAllByUser(user).isEmpty()){
            makeOnboardingFeed(user)
        }

        val accessToken = jwtTokenUtil.generateToken(TokenType.ACCESS_TOKEN, user, jwtSecret)
        val refreshToken = jwtTokenUtil.generateToken(TokenType.REFRESH_TOKEN, user, jwtSecret)


        val REFRESH_TOKEN_EXPIRATION_MS: Int = 14 * 24 * 60 * 60 * 1000
        val expirationTime = LocalDateTime.now().plus(REFRESH_TOKEN_EXPIRATION_MS.toLong(), ChronoUnit.MILLIS)
        userRefreshTokenRepository.save(UserRefreshToken.of(refreshToken, user, expirationTime))

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
        return userRepository.findBySocialTypeAndSocialUserId(socialType, socialUserId).takeIf { user -> user?.deletedAt == null } ?:
            userRepository.save(
                User(
                    nickname = nickname,
                    socialType = socialType,
                    socialUserId = socialUserId,
                    role = Role.USER
                )
            )
    }

    private fun upsertApple(socialUserId: String, nickname: String): User {
        return userRepository.findAppleUser(socialUserId)
            ?: userRepository.save(
                User(
                    nickname = nickname,
                    socialType = SocialType.APPLE,
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

        logger().info("refresh token expired ???? : ${userRefreshToken.tokenExpirationTime}")
        if(userRefreshToken.tokenExpirationTime!!.isBefore(LocalDateTime.now())){
            throw ApplicationException(ErrorCode.TOKEN_EXPIRED, "만료된 refresh token 입니다.")
        }

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
        if(folderRepository.findAllByUser(user).isEmpty()){
            makeOnboardingFeed(user)
        }

        val accessToken = jwtTokenUtil.generateToken(TokenType.ACCESS_TOKEN, user, jwtSecret)
        val refreshToken = jwtTokenUtil.generateToken(TokenType.REFRESH_TOKEN, user, jwtSecret)

        userRefreshTokenRepository.save(UserRefreshToken.of(refreshToken, user, getExpirationDateTime()))

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

            val user: User = upsertApple(socialId, name)
            userRepository.save(user)
            makeOnboardingFeed(user)

            val accessToken = jwtTokenUtil.generateToken(TokenType.ACCESS_TOKEN, user, jwtSecret)
            val refreshToken = jwtTokenUtil.generateToken(TokenType.REFRESH_TOKEN, user, jwtSecret)
            userRefreshTokenRepository.save(UserRefreshToken.of(refreshToken, user, getExpirationDateTime()))

            return TokenResponseDto(accessToken, refreshToken);
        }

        // 이미 가입한 경우
        val requestUser = userRepository.findBySocialTypeAndSocialUserId(SocialType.APPLE, socialUserId)
            ?: throw ApplicationException(ErrorCode.USER_NOT_FOUND, "가입하지 않은 유저입니다.")

        return generateTokenDto(requestUser)
    }

    @Transactional
    fun appleLoginWeb(code: String): TokenResponseDto? {
        val appleInfo = getAppleInfo(code)

        // 첫 가입인 경우
        val findUser = userRepository.findBySocialTypeAndSocialUserId(SocialType.APPLE, appleInfo.id.toString())

        if (findUser == null) {
            val user: User = upsertApple(appleInfo.id.toString(), appleInfo.email.toString())
            userRepository.save(user)
            makeOnboardingFeed(user)

            val accessToken = jwtTokenUtil.generateToken(TokenType.ACCESS_TOKEN, user, jwtSecret)
            val refreshToken = jwtTokenUtil.generateToken(TokenType.REFRESH_TOKEN, user, jwtSecret)
            userRefreshTokenRepository.save(UserRefreshToken.of(refreshToken, user, getExpirationDateTime()))

            return TokenResponseDto(accessToken, refreshToken);
        }

        // 이미 가입한 경우
        val requestUser = userRepository.findBySocialTypeAndSocialUserId(SocialType.APPLE, appleInfo.id.toString())
            ?: throw ApplicationException(ErrorCode.USER_NOT_FOUND, "가입하지 않은 유저입니다.")

        return generateTokenDto(requestUser)
    }

    fun getAppleInfo(code: String?): AppleDto {
        if (code == null) throw Exception("code is null")

        val clientSecret = createClientSecret()
        var userId = ""
        var email = ""
        var accessToken = ""

        try {
            val headers = HttpHeaders().apply {
                add("Content-type", "application/x-www-form-urlencoded")
            }

            val params: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>().apply {
                add("grant_type", "authorization_code")
                add("client_id", appleWebClientId)
                add("client_secret", clientSecret)
                add("code", code)
                add("redirect_uri", appleWebRedirectUrl)
            }

            val restTemplate = RestTemplate()
            val httpEntity = HttpEntity(params, headers)

            val response = restTemplate.exchange(
                "${APPLE_AUTH_URL}/auth/token",
                HttpMethod.POST,
                httpEntity,
                String::class.java
            )
            logger().info("/auth/token response: ${response}")

            val jsonParser = JSONParser()
            val jsonObj = jsonParser.parse(response.body) as JSONObject
            logger().info("jsonObj : ${jsonObj}")

            accessToken = jsonObj["access_token"].toString()

            // ID TOKEN을 통해 회원 고유 식별자 받기
            val signedJWT = SignedJWT.parse(jsonObj["id_token"].toString())
            val payload = signedJWT.jwtClaimsSet
            logger().info("jwtClaimSet: ${payload}")

            userId = payload.getStringClaim("sub")
            email = payload.getStringClaim("email")
            logger().info("userId: ${userId}, email: ${email}, accessToken: ${accessToken}")
        } catch (e: Exception) {
            throw Exception("/auth/token API call failed")
        }

        return AppleDto(
            id = userId,
            token = accessToken,
            email = email
        )
    }

    private fun createClientSecret(): String {
        val header = JWSHeader.Builder(JWSAlgorithm.ES256).keyID(appleLoginKey).build()
        val now = Date()

        val claimsSet = JWTClaimsSet.Builder()
            .issuer(appleTeamId)
            .issueTime(now)
            .expirationTime(Date(now.time + 3600000))
            .audience(APPLE_AUTH_URL)
            .subject(appleWebClientId)
            .build()

        val jwt = SignedJWT(header, claimsSet)

        try {
            val ecPrivateKey = getPrivateKey()
            val jwsSigner = ECDSASigner(ecPrivateKey)

            jwt.sign(jwsSigner)
        } catch (e: InvalidKeyException) {
            throw Exception("Failed create client secret")
        } catch (e: JOSEException) {
            throw Exception("Failed create client secret")
        }

        return jwt.serialize()
    }

    @Throws(Exception::class)
    private fun getPrivateKey(): ECPrivateKey {
        val keyName = "applekey.p8"

        return try {
            val s3Object = amazonS3.getObject(bucket, keyName)
            val keyBytes = s3Object.objectContent.readAllBytes()

            // PEM 읽기
            val pemParser = PEMParser(StringReader(String(keyBytes)))
            val privateKeyInfo = pemParser.readObject() as PrivateKeyInfo

            // 변환
            val converter = JcaPEMKeyConverter()
            converter.getPrivateKey(privateKeyInfo) as ECPrivateKey
        } catch (e: Exception) {
            logger().error("Failed to parse private key", e)
            throw e
        }
    }

    @Transactional
    fun makeOnboardingFeed(user: User){
        val onboardingFolder = Folder(
            name = "블링크 소개",
            user = user,
            count = 1,
            isUnclassified = false
        )
        val folder = folderRepository.save(onboardingFolder)

        val onboardingFeed = Feed(
            folder = folder,
            originUrl = onboarding_title,
            summary = onboarding_summary,
            title =  onboarding_title,
            platform = Source.ONBOARDING.source,
            status = Status.SAVED,
            isChecked = true,
            isMarked = true,
        )
        feedRepository.save(onboardingFeed)

        val onboardingKeyword = Keyword(
            feed = onboardingFeed,
            content = onboarding_keyword
        )
        keywordRepository.save(onboardingKeyword)
    }

    fun generateTokenDto(user: User): TokenResponseDto{
        val accessToken = jwtTokenUtil.generateToken(TokenType.ACCESS_TOKEN, user, jwtSecret)
        val refreshToken = jwtTokenUtil.generateToken(TokenType.REFRESH_TOKEN, user, jwtSecret)
        userRefreshTokenRepository.save(UserRefreshToken.of(refreshToken, user, getExpirationDateTime()))
        return TokenResponseDto(accessToken, refreshToken)
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

    // 최초 로그인이 아닌 경우 > code와 id_token 교환
    fun exchangeCodeForToken(code: String): TokenResponse {
        val tokenUrl = "https://appleid.apple.com/auth/token"
        val clientId = "CLIENT_ID" // TODO: 추가
        val clientSecret = generateClientSecret()

        val body = LinkedMultiValueMap<String, String>().apply {
            add("client_id", clientId)
            add("client_secret", clientSecret)
            add("code", code)
            add("grant_type", "authorization_code")
        }

        val response = restTemplate.postForObject(tokenUrl, HttpEntity(body, HttpHeaders()), TokenResponse::class.java)
        return response ?: throw ApplicationException(ErrorCode.TOKEN_EXCHANGE_FAILED, "apple token 교환 실패")
    }

    fun generateClientSecret(): String {
        val teamId = teamId
        val clientId = appleClientId
        val keyId = loginKey
        val privateKeyPath = keyPath

        // 현재 시간과 만료 시간 설정 (기본적으로 클라이언트 시크릿은 6개월간 유효)
        val now = Instant.now()
        val expirationTime = now.plusSeconds(180 * 24 * 60 * 60) // 180일(6개월) 유효

        // 개인 키 파일 로드
        // val privateKeyBytes = Files.readAllBytes(Paths.get(privateKeyPath!!))
        // val privateKey = String(privateKeyBytes)
        logger().info("privateKey : ${privateKeyPath!!.take(5)}******")
        val privateKey: ECPrivateKey = convertStringToECPrivateKey(privateKeyPath)!!


        // JWT 생성
        return JWT.create()
            .withIssuer(teamId)
            .withIssuedAt(Date.from(now))
            .withExpiresAt(Date.from(expirationTime))
            .withAudience("https://appleid.apple.com")
            .withSubject(clientId)
            .sign(Algorithm.ECDSA256(null, privateKey))
    }

    @Throws(java.lang.Exception::class)
    fun convertStringToECPrivateKey(privateKeyStr: String?): ECPrivateKey? {
        val privateKeyBytes = Base64.getDecoder().decode(privateKeyStr)
        val keySpec = PKCS8EncodedKeySpec(privateKeyBytes)
        val keyFactory = KeyFactory.getInstance("EC")
        return keyFactory.generatePrivate(keySpec) as ECPrivateKey
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

    @Transactional
    fun logout(refreshToken: String, userAccount: UserAccount) {
        val user = userRepository.findById(userAccount.userId)
            .orElseThrow { ApplicationException(ErrorCode.USER_NOT_FOUND, "일치하는 유저가 없습니다 : ${userAccount.userId}", Throwable()) }
        val userRefreshTokens = userRefreshTokenRepository.findByUserId(user.id!!)
        userRefreshTokens.forEach { refreshToken ->
            refreshToken.expire()
            userRefreshTokenRepository.save(refreshToken)
        }
    }

    @Transactional
    fun signout(userAccount: UserAccount) {
        val user = userRepository.findById(userAccount.userId)
            .orElseThrow { ApplicationException(ErrorCode.USER_NOT_FOUND, "일치하는 유저가 없습니다 : ${userAccount.userId}", Throwable()) }
        user.updateDeletedAt()
        user.updateSocialId()
        userRepository.save(user)

        val userRefreshTokens = userRefreshTokenRepository.findByUserId(user.id!!)
        userRefreshTokens.forEach { userRefreshToken ->
            userRefreshToken.expire() // 토큰 만료 시간 설정
            userRefreshTokenRepository.save(userRefreshToken) // 변경된 토큰 저장
        }
    }

    private fun getExpirationDateTime(): LocalDateTime {
        val REFRESH_TOKEN_EXPIRATION_MS: Int = 14 * 24 * 60 * 60 * 1000
        return LocalDateTime.now().plus(REFRESH_TOKEN_EXPIRATION_MS.toLong(), ChronoUnit.MILLIS)
    }

    companion object{
        const val onboarding_summary = "블링크를 활용하는 방법을 정리했습니다. 글 추가부터 똑똑하게 활용하는 방법을 모두 알려드릴게요!"
        const val onboarding_title = "어서오세요, 블링크는 처음이시죠"
        const val onboarding_keyword = "블링크 설명서"
        const val APPLE_AUTH_URL = "https://appleid.apple.com"
    }
}