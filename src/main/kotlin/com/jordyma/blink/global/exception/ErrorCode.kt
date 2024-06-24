package com.jordyma.blink.global.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(val errorCode: String, val statusCode: HttpStatus) {
    UNAUTHORIZED("A000", HttpStatus.UNAUTHORIZED),
}