package com.jordyma.blink.global.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.*


class JwtAuthenticationFilter(private val authenticationManager: AuthenticationManager, private val permitUrls: Array<String>): OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = getToken(request)
        val authentication: Authentication = UsernamePasswordAuthenticationToken(token, "")
        val authenticatedAuthentication: Authentication = authenticationManager.authenticate(authentication)
        if (authenticatedAuthentication.isAuthenticated) {
            SecurityContextHolder.getContext().authentication = authenticatedAuthentication
            filterChain.doFilter(request, response)
        } else {
            SecurityContextHolder.clearContext()
        }
    }

    private fun getToken(request: HttpServletRequest): String? {
        val authorizationHeader = request.getHeader("Authorization") ?: return null
        return authorizationHeader.substring("Bearer ".length)
    }

    @Throws(ServletException::class)
    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI
        return isPermitUrl(path)
    }

    private fun isPermitUrl(path: String): Boolean {
        val antPathMatcher = AntPathMatcher()
        return Arrays.stream(this.permitUrls).anyMatch { permitUrl ->
            antPathMatcher.match(
                permitUrl,
                path
            )
        }

    }
}