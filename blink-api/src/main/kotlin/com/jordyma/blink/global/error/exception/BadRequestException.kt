package com.jordyma.blink.global.error.exception

import com.jordyma.blink.global.error.ErrorCode

class BadRequestException(errorCode: ErrorCode) : RuntimeException(errorCode.message) {
    private val code: String = errorCode.code
}