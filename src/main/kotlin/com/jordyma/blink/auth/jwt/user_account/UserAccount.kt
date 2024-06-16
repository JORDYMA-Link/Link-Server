package com.jordyma.blink.auth.jwt.user_account

import com.jordyma.blink.user.entity.Role
import org.springframework.security.core.userdetails.User


class UserAccount(userId: Long?, nickName: String?, role: Role?) :
    User(userId.toString(), "", object : ArrayList<Role?>() {
        init {
            add(role)
        }
    }) {

    var userId: Long? = null

    var nickName: String? = null

    val role: Role? = null
}


