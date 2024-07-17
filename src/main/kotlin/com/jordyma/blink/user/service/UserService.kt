package com.jordyma.blink.user.service

import com.jordyma.blink.global.error.ID_NOT_FOUND
import com.jordyma.blink.global.error.exception.IdRequiredException
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.user.dto.UserInfoDto
import com.jordyma.blink.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository
) {
    @Transactional(readOnly = true)
    fun find(userId: Long): UserInfoDto {
        val user =
            userRepository.findById(userId).orElseThrow { ApplicationException(ErrorCode.NOT_FOUND, "사용자를 찾을 수 없습니다") }
        return UserInfoDto(
            id = user.id ?: throw IdRequiredException(ID_NOT_FOUND),
            name = user.nickname
        )
    }
}