package com.jordyma.blink.user.repository

import com.jordyma.blink.user.entity.SocialType
import com.jordyma.blink.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findBySocialTypeAndSocialUserId(socialType: SocialType, socialUserId: String): User?
}