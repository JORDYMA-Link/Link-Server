package com.jordyma.blink.user.service

import com.jordyma.blink.user.entity.User
import com.jordyma.blink.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserService (
    val userRepository: UserRepository,
){
    @Transactional(readOnly = true)
    fun find(userId: Long): UserInfoDto {
        val user = userRepository.findById(userId).orElseThrow { BadRequestException(USER_NOT_FOUND) }
        return UserInfoDto(
            id = user.id ?: throw IdRequiredException(ID_NOT_FOUND),
            name = user.nickname
        )
    }

    fun find(userId: Long): User {
        return userRepository.getById(userId)
    }
}