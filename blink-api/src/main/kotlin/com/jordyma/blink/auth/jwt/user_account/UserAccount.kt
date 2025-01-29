package com.jordyma.blink.auth.jwt.user_account

import com.jordyma.blink.user.Role
import org.springframework.security.core.userdetails.User


class UserAccount(userId: Long, nickName: String, role: Role) :
    User(userId.toString(), "", object : ArrayList<Role?>() {
        init {
            add(role)
        }
    }) {

    val userId: Long = userId;

    val nickName: String = nickName;

    val role: Role = Role.USER;
}


