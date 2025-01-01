package com.jordyma.blink.user.repository

import com.jordyma.blink.folder.entity.Folder
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.user.entity.SocialType
import com.jordyma.blink.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findBySocialTypeAndSocialUserId(socialType: SocialType, socialUserId: String): User?

    @Query("select u from user u where u.socialType ='APPLE' and u.socialUserId =:socialUserId")
    fun findAppleUser(socialUserId: String): User?

    override fun getById(id: Long): User =
        findById(id).orElseThrow() { ApplicationException(ErrorCode.NOT_FOUND, "일치하는 유저가 없습니다 : $id", Throwable()) }
}