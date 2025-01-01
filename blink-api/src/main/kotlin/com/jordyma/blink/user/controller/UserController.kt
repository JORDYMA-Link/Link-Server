package com.jordyma.blink.user.controller

import com.jordyma.blink.auth.jwt.user_account.UserAccount
import com.jordyma.blink.user.dto.request.UpdateUserProfileReqDto
import com.jordyma.blink.user.dto.request.UpdateUserPushTokenRequestDto
import com.jordyma.blink.user.dto.response.UserProfileResDto
import com.jordyma.blink.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@Tag(name = "user", description = "유저 API")
@RestController
@RequestMapping("/user")
class UserController (
    private val userService: UserService,
){
    @Operation(summary = "유저 프로필 조회 api")
    @GetMapping("/profile")
    fun getUserProfile(
        @AuthenticationPrincipal userAccount: UserAccount,
    ): ResponseEntity<UserProfileResDto> {
        val response: UserProfileResDto = userService.getProfile(userAccount)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "유저 프로필 수정 api")
    @PatchMapping("/profile")
    fun updateUserProfile(
        @AuthenticationPrincipal userAccount: UserAccount,
        @RequestBody requestDto: UpdateUserProfileReqDto,
    ): ResponseEntity<UserProfileResDto> {
        val response: UserProfileResDto = userService.updateProfile(userAccount, requestDto.nickname)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "유저 푸시 토큰 갱신 api")
    @PutMapping("/push-token")
    fun updatePushToken(
        @AuthenticationPrincipal userAccount: UserAccount,
        @RequestBody updateUserPushTokenRequestDto: UpdateUserPushTokenRequestDto,
    ): ResponseEntity<Unit> {
        userService.updatePushToken(userAccount, updateUserPushTokenRequestDto)
        return ResponseEntity.ok().build()
    }

}