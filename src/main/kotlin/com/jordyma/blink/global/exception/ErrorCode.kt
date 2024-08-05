package com.jordyma.blink.global.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(val errorCode: String, val statusCode: HttpStatus) {

    // Common
    SHOULD_NOT_NULL("C000", HttpStatus.NOT_FOUND),
    NOT_FOUND("C001", HttpStatus.NOT_FOUND),

    // Authentication
    UNAUTHORIZED("A000", HttpStatus.UNAUTHORIZED),
    TOKEN_VERIFICATION_EXCEPTION("A001", HttpStatus.UNAUTHORIZED),

    // External
    OPENKEY_NOT_MATCHED("E000", HttpStatus.INTERNAL_SERVER_ERROR),
    NONCE_NOT_MATCHED("E0001", HttpStatus.INTERNAL_SERVER_ERROR),
   // NOT_FOUND("E0002", HttpStatus.NOT_FOUND),

    // Gemini
    JSON_NOT_FOUND("G0000", HttpStatus.NO_CONTENT),

    // User
    USER_NOT_FOUND("U000", HttpStatus.NOT_FOUND),

    // Folder
    NOT_CREATED("F000", HttpStatus.INTERNAL_SERVER_ERROR),

    // Feed
    FEED_NOT_FOUND("FD000", HttpStatus.NOT_FOUND),
}