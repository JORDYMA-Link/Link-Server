package com.jordyma.blink.global.handler

import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorResponse
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApplicationExceptionHandler {

    @ExceptionHandler(ApplicationException::class)
    fun handleCommonException(e: ApplicationException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            code = e.code.errorCode,
            message = e.message,
            detail = e.throwable?.message
        )
        println("ApplicationExceptionHandler.handleCommonException: $errorResponse")
        return ResponseEntity(errorResponse, e.code.statusCode)
    }
}