package com.jordyma.blink.auth.controller
import com.jordyma.blink.auth.service.AuthService
import com.jordyma.blink.auth.dto.request.KakaoLoginRequestDto
import com.jordyma.blink.auth.dto.response.TokenResponseDto
import com.jordyma.blink.global.util.CommonUtil
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

//@Tag(name = "auth", description = "인증 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/kakao-login")
//    @Operation(summary = "카카오 로그인 API", description = "카카오 idtoken을 입력받아 소셜 로그인을 진행")
    fun kakaoLogin(
        @Validated @RequestBody kakaoLoginRequestDto: KakaoLoginRequestDto
    ): ResponseEntity<TokenResponseDto> {
        return ResponseEntity.ok(authService.kakaoLogin(kakaoLoginRequestDto))
    }

    @PostMapping("/regenerate-token")
//    @Operation(summary = "토큰 재발급 API", description = "기존 토큰을 입력받아 새로운 토큰을 발급")
    fun regeneratedToken(
       /*  @Schema(hidden = true) */ @RequestHeader("Authorization") authorizationHeader: String?
    ): ResponseEntity<TokenResponseDto> {
        val token: String? = CommonUtil.parseTokenFromBearer(authorizationHeader)

        val tokenResponseDto: TokenResponseDto = authService.regenerateToken(token)

        return ResponseEntity.ok(tokenResponseDto)
    }

    @GetMapping("/kakao-login-web/callback")
    fun kakaoLoginWeb(
        @RequestParam("code") code: String
    ): ResponseEntity<TokenResponseDto> {
        return ResponseEntity.ok(authService.kakaoLoginWeb(code))
    }
}