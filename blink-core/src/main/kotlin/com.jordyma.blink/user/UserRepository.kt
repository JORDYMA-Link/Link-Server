package com.jordyma.blink.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findBySocialTypeAndSocialUserId(socialType: SocialType, socialUserId: String): User?

    @Query("select u from user u where u.socialType ='APPLE' and u.socialUserId =:socialUserId")
    fun findAppleUser(socialUserId: String): User?

//    override fun getById(id: Long): User =
//        findById(id).orElseThrow() { ApplicationException(ErrorCode.NOT_FOUND, "일치하는 유저가 없습니다 : $id", Throwable()) }
}