package com.jordyma.blink.User.repository

import com.jordyma.blink.User.entity.SocialType
import com.jordyma.blink.User.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findBySocialTypeAndSocialUserId(socialType: SocialType, socialUserId: String): User?
}