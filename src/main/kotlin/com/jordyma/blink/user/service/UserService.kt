package com.jordyma.blink.user.service

import com.jordyma.blink.user.entity.User
import com.jordyma.blink.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService (
    val userRepository: UserRepository,
){
    fun find(userId: Long): User {
        return userRepository.getById(userId)
    }
}