package com.jordyma.blink.auth.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.jordyma.blink.auth.dto.response.AppleDto
import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.ECDSASigner
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import net.minidev.json.JSONObject
import net.minidev.json.parser.JSONParser
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import sun.security.ec.ECPrivateKeyImpl
import java.security.InvalidKeyException
import java.security.KeyFactory
import java.security.interfaces.ECPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

@Service
class AppleService(

    @Value("\${apple.team.id}") private val appleTeamId: String,
    @Value("\${apple.login.key}") private val appleLoginKey: String,
    @Value("\${apple.client.id}") private val appleClientId: String,
    @Value("\${apple.redirect.url}") private val appleRedirectUrl: String,
    @Value("\${apple.key.path}") private val appleKeyPath: String
){
    fun getAppleInfo(code: String?): AppleDto {
        if (code == null) throw Exception("Failed get authorization code")

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
                add("client_id", appleClientId)
                add("client_secret", clientSecret)
                add("code", code)
                add("redirect_uri", appleRedirectUrl)
            }

            val restTemplate = RestTemplate()
            val httpEntity = HttpEntity(params, headers)

            val response = restTemplate.exchange(
                "$APPLE_AUTH_URL/auth/token",
                HttpMethod.POST,
                httpEntity,
                String::class.java
            )

            val jsonParser = JSONParser()
            val jsonObj = jsonParser.parse(response.body) as JSONObject

            accessToken = jsonObj["access_token"].toString()

            // ID TOKEN을 통해 회원 고유 식별자 받기
            val signedJWT = SignedJWT.parse(jsonObj["id_token"].toString())
            val getPayload = signedJWT.jwtClaimsSet

            val objectMapper = ObjectMapper()
            val payload = objectMapper.readValue(getPayload.toJSONObject().toString(), JSONObject::class.java)

            userId = payload["sub"].toString()
            email = payload["email"].toString()
        } catch (e: Exception) {
            throw Exception("API call failed")
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
            .subject(appleClientId)
            .build()

        val jwt = SignedJWT(header, claimsSet)

        try {
            val keySpec = PKCS8EncodedKeySpec(appleKeyPath.toByteArray())
            val keyFactory = KeyFactory.getInstance("EC")
            val ecPrivateKey = keyFactory.generatePrivate(keySpec) as ECPrivateKey

            val jwsSigner = ECDSASigner(ecPrivateKey)

            jwt.sign(jwsSigner)
        } catch (e: InvalidKeyException) {
            throw Exception("Failed create client secret")
        } catch (e: JOSEException) {
            throw Exception("Failed create client secret")
        }

        return jwt.serialize()
    }

    companion object {
        private const val APPLE_AUTH_URL = "https://appleid.apple.com"
    }

    fun getAppleLogin(): String {
        return "$APPLE_AUTH_URL/auth/authorize" +
                "?client_id=$appleClientId" +
                "&redirect_uri=$appleRedirectUrl" +
                "&response_type=code id_token&scope=name email&response_mode=form_post"
    }
}