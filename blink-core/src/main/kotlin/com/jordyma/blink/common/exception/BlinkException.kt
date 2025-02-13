package com.jordyma.blink.common.exception


class BlinkException(val code: ErrorCode, override val message: String, val throwable: Throwable? = null): RuntimeException(message) {
}

enum class ErrorCode {
    USER_NOT_FOUND,
}