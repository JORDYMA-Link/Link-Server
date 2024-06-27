package com.jordyma.blink.user.service

import com.jordyma.blink.global.error.USER_NOT_FOUND
import com.jordyma.blink.global.error.exception.BadRequestException
import com.jordyma.blink.user.entity.User
import com.jordyma.blink.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService @Autowired constructor(
    private val userRepository: UserRepository
){
    @Transactional(readOnly = true)
    fun find(userId: Long): User {
        return userRepository.findById(userId).orElseThrow { BadRequestException(USER_NOT_FOUND) }
    }
}