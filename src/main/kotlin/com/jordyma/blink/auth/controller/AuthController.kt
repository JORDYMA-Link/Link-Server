package com.jordyma.blink.auth.controller
import com.jordyma.blink.auth.dto.State
import com.jordyma.blink.auth.dto.request.KakaoLoginRequestDto
import com.jordyma.blink.auth.dto.request.AppleLoginRequestDto
import com.jordyma.blink.auth.dto.response.TokenResponseDto
import com.jordyma.blink.auth.service.AuthService
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.global.util.CommonUtil
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.serialization.json.*
import lombok.RequiredArgsConstructor
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.net.URL
import java.util.*


@Tag(name = "auth", description = "인증 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/kakao-login")
    @Operation(summary = "카카오 로그인 API", description = "카카오 idtoken을 입력받아 소셜 로그인을 진행")
    fun kakaoLogin(
        @Validated @RequestBody kakaoLoginRequestDto: KakaoLoginRequestDto
    ): ResponseEntity<TokenResponseDto> {
        return ResponseEntity.ok(authService.kakaoLogin(kakaoLoginRequestDto))
    }

    @PostMapping("/apple-login")
    @Operation(summary = "애플 로그인 API", description = "애플 idtoken을 입력받아 소셜 로그인을 진행")
    fun appleLogin(
        @Validated @RequestBody appleLoginRequestDto: AppleLoginRequestDto
    ): ResponseEntity<TokenResponseDto> {
        return ResponseEntity.ok(authService.appleLogin(appleLoginRequestDto))
    }

    @PostMapping("/regenerate-token")
    @Operation(summary = "토큰 재발급 API", description = "기존 리프레시 토큰을 입력받아 새로운 토큰을 발급")
    fun regeneratedToken(
       @Schema(hidden = true) @RequestHeader("Authorization") authorizationHeader: String?
    ): ResponseEntity<TokenResponseDto> {
        val token: String = CommonUtil.parseTokenFromBearer(authorizationHeader) ?: throw ApplicationException(ErrorCode.TOKEN_VERIFICATION_EXCEPTION, "올바르지 않은 토큰 형식입니다.")

        val tokenResponseDto: TokenResponseDto = authService.regenerateToken(token)

        return ResponseEntity.ok(tokenResponseDto)
    }

    @GetMapping("/kakao-login-web/callback")
    fun kakaoLoginWeb(
        @RequestParam("code") code: String,
        @RequestParam("state") state: String,
    ): ResponseEntity<Void> {
        val base64Decoder = Base64.getUrlDecoder()
        val jsonFormat = Json { prettyPrint = true }
        val jsonString = base64Decoder.decode(state).toString(Charsets.UTF_8)
        val stateInfo: State = runCatching {
            jsonFormat.decodeFromString<State>(jsonString)
        }.getOrElse {
            State(
                // TODO 수정
                webRedirectUrl = "https://blink.jordyma.com",
            )
        }
        val webRedirectUrl = stateInfo.webRedirectUrl
        val tokenInfo = authService.kakaoLoginWeb(code)
        val uri = webRedirectUrl.toHttpUrlOrNull()!!.newBuilder()
            .addQueryParameter("accessToken", tokenInfo.accessToken)
            .addQueryParameter("refreshToken", tokenInfo.refreshToken)
            .build()

        val headers = HttpHeaders()
        headers.location = uri.toUri()
        return ResponseEntity<Void>(headers, HttpStatus.FOUND)
    }
}