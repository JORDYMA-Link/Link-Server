package com.jordyma.blink.user.service

import com.jordyma.blink.auth.jwt.user_account.UserAccount
import com.jordyma.blink.user.dto.response.UserProfileResDto
import com.jordyma.blink.global.error.ID_NOT_FOUND
import com.jordyma.blink.global.error.exception.IdRequiredException
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.user.dto.UserInfoDto
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
            // email = user.
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
}