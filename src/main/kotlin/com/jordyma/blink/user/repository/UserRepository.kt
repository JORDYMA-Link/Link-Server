package com.jordyma.blink.user.repository

import com.jordyma.blink.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
}