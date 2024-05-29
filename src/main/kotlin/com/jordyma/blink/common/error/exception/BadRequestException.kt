package com.jordyma.blink.common.error.exception

import com.jordyma.blink.common.error.ErrorCode

class BadRequestException(errorCode: ErrorCode) : RuntimeException(errorCode.message) {
    private val code: String = errorCode.code
}