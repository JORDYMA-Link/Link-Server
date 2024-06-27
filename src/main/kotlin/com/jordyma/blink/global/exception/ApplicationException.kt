package com.jordyma.blink.global.exception

class ApplicationException(val code: ErrorCode, override val message: String, val throwable: Throwable? = null): RuntimeException(message) {
}
