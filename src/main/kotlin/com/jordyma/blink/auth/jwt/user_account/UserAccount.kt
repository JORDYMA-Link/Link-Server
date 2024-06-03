package com.jordyma.blink.auth.jwt.user_account

import com.jordyma.blink.User.entity.Role
import org.springframework.security.core.userdetails.User


class UserAccount(userId: Long?, profileUrl: String?, nickName: String?, role: Role?) :
    User(userId.toString(), "", object : ArrayList<Role?>() {
        init {
            add(role)
        }
    }) {

    var userId: Long? = null

    var profileUrl: String? = null

    var nickName: String? = null
}


