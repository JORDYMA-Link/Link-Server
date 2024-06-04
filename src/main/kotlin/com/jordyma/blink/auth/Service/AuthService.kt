package com.jordyma.blink.auth.Service
import com.jordyma.blink.User.entity.Role
import com.jordyma.blink.User.entity.SocialType
import com.jordyma.blink.User.entity.User
import com.jordyma.blink.User.repository.UserRepository
import com.jordyma.blink.auth.dto.request.KakaoLoginRequestDto
import com.jordyma.blink.auth.dto.response.TokenResponseDto
import com.jordyma.blink.auth.jwt.enums.TokenType
import com.jordyma.blink.auth.jwt.util.JwtTokenUtil
import com.jordyma.blink.user_refresh_token.entity.UserRefreshToken
import com.jordyma.blink.user_refresh_token.repository.UserRefreshTokenRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Header
import io.jsonwebtoken.Jwt
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class AuthService(
    private val jwtTokenUtil: JwtTokenUtil,
    private val userRepository: UserRepository,
    private val userRefreshTokenRepository: UserRefreshTokenRepository
) {

    @Value("\${kakao.auth.jwt.aud}")
    var aud: String? = null

    @Value("\${kakao.auth.jwt.iss}")
    var iss: String? = null

    @Transactional
    fun kakaoLogin(kakaoLoginRequestDto: KakaoLoginRequestDto): TokenResponseDto {
        val idToken: String = kakaoLoginRequestDto.idToken;
        val claims: Jwt<Header<*>, Claims> = jwtTokenUtil.parseJwt(idToken)

        val kid: String = claims.header.get("kid").toString()
        jwtTokenUtil.verifySignature(idToken, kid, aud, iss, kakaoLoginRequestDto.nonce)

        val nickname: String = claims.body.get("nickname", String::class.java)
        val socialUserId: String = claims.body.get("sub", String::class.java)

        val user: User = upsertUser(SocialType.KAKAO, socialUserId, nickname)

        val accessToken = jwtTokenUtil.generateToken(TokenType.ACCESS_TOKEN, user)
        val refreshToken = jwtTokenUtil.generateToken(TokenType.REFRESH_TOKEN, user)

        userRefreshTokenRepository.save(UserRefreshToken.of(refreshToken, user))

        return TokenResponseDto(accessToken, refreshToken);
    }

    private fun upsertUser(socialType: SocialType, socialUserId: String, nickname: String): User {
        return userRepository.findBySocialTypeAndSocialUserId(socialType, socialUserId)
            ?: userRepository.save(User.of(nickname, SocialType.KAKAO, socialUserId, Role.USER))
    }

    @Transactional
    fun regenerateToken(token: String?): TokenResponseDto {
        val claims = jwtTokenUtil.parseToken(token)
        val tokenType: TokenType = TokenType.valueOf(claims!!.body["type", String::class.java])

        if (tokenType !== TokenType.REFRESH_TOKEN) {
//            throw ApplicationException(ErrorCode.TOKEN_VERIFICATION_EXCEPTION)
            throw Exception("TOKEN_VERIFICATION_EXCEPTION")
        }

        val userRefreshToken: UserRefreshToken = userRefreshTokenRepository.findByRefreshToken(token)
//            .orElseThrow { ApplicationException(ErrorCode.TOKEN_NOT_FOUND) }
            ?: throw Exception("TOKEN_NOT_FOUND")
        val subject = claims.body.subject
        val user: User = userRepository.findById(subject.toLong())
//            .orElseThrow { ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION) }
            .orElseThrow { Exception("NOT_FOUND_EXCEPTION") }
        val accessToken = jwtTokenUtil.generateToken(TokenType.ACCESS_TOKEN, user)
        val refreshToken = jwtTokenUtil.generateToken(TokenType.REFRESH_TOKEN, user)

        userRefreshToken.updateRefreshToken(refreshToken)

        return TokenResponseDto(accessToken, refreshToken)
    }
}