package com.jordyma.blink.global.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(val errorCode: String, val statusCode: HttpStatus) {

    // Common
    NOT_FOUND("C000", HttpStatus.NOT_FOUND),

    // Authentication
    UNAUTHORIZED("A000", HttpStatus.UNAUTHORIZED),
    TOKEN_VERIFICATION_EXCEPTION("A001", HttpStatus.UNAUTHORIZED),

    // External
    OPENKEY_NOT_MATCHED("E000", HttpStatus.INTERNAL_SERVER_ERROR),
    NONCE_NOT_MATCHED("E0001", HttpStatus.INTERNAL_SERVER_ERROR),

    // User
    USER_NOT_FOUND("U000", HttpStatus.NOT_FOUND),
}