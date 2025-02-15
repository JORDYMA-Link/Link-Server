package com.jordyma.blink.auth.controller
import com.jordyma.blink.auth.dto.State
import com.jordyma.blink.auth.dto.request.KakaoLoginRequestDto
import com.jordyma.blink.auth.dto.request.AppleLoginRequestDto
import com.jordyma.blink.auth.dto.response.AppleUserInfo
import com.jordyma.blink.auth.dto.response.TokenResponseDto
import com.jordyma.blink.auth.jwt.user_account.UserAccount
import com.jordyma.blink.auth.service.AuthService
import com.jordyma.blink.folder.domain.service.FolderService
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.global.util.CommonUtil
import com.jordyma.blink.logger
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import kotlinx.serialization.json.*
import lombok.RequiredArgsConstructor
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@Tag(name = "auth", description = "인증 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
class AuthController(
    private val authService: AuthService,
    private val folderService: FolderService,
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
                webRedirectUrl = "https://api.blink-archive.com",
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

    @PostMapping("/apple-login-web/callback")
    fun appleLoginWeb(
        request: HttpServletRequest
    ): ResponseEntity<Void> {
        logger().info("apple login web callback api called : ${request.getParameter("code")}")

        val base64Decoder = Base64.getUrlDecoder()
        val jsonFormat = Json { prettyPrint = true }
        val jsonString = base64Decoder.decode(request.getParameter("state")).toString(Charsets.UTF_8)
        val stateInfo: State = runCatching {
            jsonFormat.decodeFromString<State>(jsonString)
        }.getOrElse {
            State(
                webRedirectUrl = "https://api.blink-archive.com",
            )
        }

        val webRedirectUrl = stateInfo.webRedirectUrl

        // resolve code
        val tokenInfo = authService.appleLoginWeb(request.getParameter("code"))

        val uri = webRedirectUrl.toHttpUrlOrNull()!!.newBuilder()
            .addQueryParameter("accessToken", tokenInfo?.accessToken)
            .addQueryParameter("refreshToken", tokenInfo?.refreshToken)
            .build()

        val headers = HttpHeaders()
        headers.location = uri.toUri()
        return ResponseEntity<Void>(headers, HttpStatus.FOUND)
    }

    fun parseUserJson(userJson: String): AppleUserInfo {
        return Json.decodeFromString(userJson)
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "refresh token으로만 요청 가능, 로그아웃 처리 시 db에 저장된 refresh token 만료 처리")
    fun logout(
        @AuthenticationPrincipal userAccount: UserAccount,
        @RequestHeader(value = "Authorization") refreshToken: String
    ): ResponseEntity<String> {
        authService.logout(refreshToken, userAccount)
        return ResponseEntity.ok().body("로그아웃이 완료되었습니다.")
    }

    @PostMapping("/signout")
    @Operation(summary = "탈퇴하기", description = "탈퇴 처리 시 refresh token 만료 처리")
    fun logout(
        @AuthenticationPrincipal userAccount: UserAccount,
    ): ResponseEntity<String> {
        folderService.signOutDelete(userAccount.userId)
        authService.signout(userAccount)
        return ResponseEntity.ok().body("탈퇴가 완료되었습니다.")
    }
}
