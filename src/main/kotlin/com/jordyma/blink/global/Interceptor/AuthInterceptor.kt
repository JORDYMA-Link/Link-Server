package com.jordyma.blink.global.Interceptor

import com.jordyma.blink.auth.jwt.user_account.UserAccount
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.user.service.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor


@Component
class AuthInterceptor(
    private val userService: UserService
) : HandlerInterceptor {

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication

        if (authentication != null && authentication.isAuthenticated) {
            val userAccount = authentication.principal as UserAccount

            // 탈퇴한 유저인지 확인
            if (userService.isDeletedUser(userAccount)) {
                throw ApplicationException(ErrorCode.USER_SIGNED_OUT, "탈퇴한 유저입니다.")
                return false
            }
        }

        return true
    }
}