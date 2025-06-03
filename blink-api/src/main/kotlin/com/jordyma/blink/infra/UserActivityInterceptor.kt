package com.jordyma.blink.infra

import com.jordyma.blink.auth.jwt.user_account.UserAccount
import com.jordyma.blink.logger
import com.jordyma.blink.stats.service.LinkStatsIncreaseService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.messaging.handler.HandlerMethod
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class UserActivityInterceptor (
    @Qualifier("linkStatsIncreaseServiceImpl")
    private val linkStatsIncreaseService: LinkStatsIncreaseService,
) : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val userId = getUserIdFromRequest()

        // 활성 사용자수 ++
        if (userId != null) {
            linkStatsIncreaseService.recordUserActivity(userId)
            logger().info("user record increment for user: {}", userId)
        }

        if (handler is HandlerMethod) {
            val method = handler.method

            // 피드 조회 api인 경우 ++
            val requestURI = request.requestURI
            if (requestURI.contains("/api/feeds/detail/") && request.method == "GET") {
                logger().info("Feed view increment for URI: {}", requestURI)
                linkStatsIncreaseService.incrementLinkView()
            }
        }

        return true
    }

    private fun getUserIdFromRequest(): Long? {
        val authentication = SecurityContextHolder.getContext().authentication

        // Filter에서 주입했던 UserAccount 객체 활용
        if (authentication != null && authentication.isAuthenticated && authentication.principal is UserAccount) {
            val userAccount = authentication.principal as UserAccount
            return userAccount.userId
        }
        return null
    }
}