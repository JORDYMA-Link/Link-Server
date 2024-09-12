package com.jordyma.blink.user.service

import com.jordyma.blink.auth.jwt.user_account.UserAccount
import com.jordyma.blink.user.dto.response.UserProfileResDto
import com.jordyma.blink.global.error.ID_NOT_FOUND
import com.jordyma.blink.global.error.exception.IdRequiredException
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.global.error.USER_NOT_FOUND
import com.jordyma.blink.user.constants.PushTokenType
import com.jordyma.blink.user.entity.User
import com.jordyma.blink.user.dto.UserInfoDto
import com.jordyma.blink.user.dto.request.UpdateUserPushTokenRequestDto
import com.jordyma.blink.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService (
    val userRepository: UserRepository,
){
    @Transactional(readOnly = true)
    fun find(userId: Long): UserInfoDto {
        val user = userRepository.findById(userId).orElseThrow { throw ApplicationException(ErrorCode.USER_NOT_FOUND, "없는 유저입니다.") }
        return UserInfoDto(
            id = user.id ?: throw IdRequiredException(ID_NOT_FOUND),
            name = user.nickname
        )
    }

    @Transactional(readOnly = true)
    fun getProfile(userAccount: UserAccount): UserProfileResDto {
        val user = userRepository.getById(userAccount.userId)
        return UserProfileResDto(
            nickName = user.nickname,
        )
    }

    @Transactional
    fun updateProfile(userAccount: UserAccount, nickName: String): UserProfileResDto {
        val user = userRepository.getById(userAccount.userId)
        user.updateNickname(nickName)
        userRepository.save(user)
        return UserProfileResDto(
            nickName = user.nickname
        )
    }

    @Transactional
    fun isDeletedUser(userAccount: UserAccount): Boolean {
        val user = userRepository.findById(userAccount.userId)
            .orElseThrow { throw ApplicationException(ErrorCode.USER_NOT_FOUND, "없는 유저입니다.") }
        return user.deletedAt != null
    }

    fun updatePushToken(userAccount: UserAccount, updateUserPushTokenRequestDto: UpdateUserPushTokenRequestDto) {
        val token = updateUserPushTokenRequestDto.pushToken;
        val pushTokenType = updateUserPushTokenRequestDto.pushTokenType;

        val user = userRepository.findById(userAccount.userId)
            .orElseThrow { throw ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.") }

        if (pushTokenType == PushTokenType.IOS) {
            user.iosPushToken = token
        } else if (pushTokenType == PushTokenType.AOS) {
            user.aosPushToken = token
        } else {
            throw ApplicationException(ErrorCode.BAD_REQUEST, "푸시 토큰 타입이 잘못되었습니다.")
        }

        userRepository.save(user)
    }
}