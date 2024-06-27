package com.jordyma.blink.global.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorResponse
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.NoArgsConstructor
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException


@NoArgsConstructor
class JwtExceptionFilter : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            doFilter(request, response, filterChain)
        } catch (e: ApplicationException) {
            val body: MutableMap<String, Any> = HashMap()
            val mapper = ObjectMapper()
            response.status = e.code.statusCode.value()
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            body.put("message", e.message)
            mapper.writeValue(response.outputStream, body)
        }
    }
}