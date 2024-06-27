package com.jordyma.blink.user.service

import com.jordyma.blink.global.error.USER_NOT_FOUND
import com.jordyma.blink.global.error.exception.BadRequestException
import com.jordyma.blink.user.dto.UserInfoDto
import com.jordyma.blink.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository
){
    @Transactional(readOnly = true)
    fun find(userId: Long): UserInfoDto {
        val user = userRepository.findById(userId).orElseThrow { BadRequestException(USER_NOT_FOUND) }
        return UserInfoDto(
            id = user.id,
            name = user.nickname
        )
    }
}