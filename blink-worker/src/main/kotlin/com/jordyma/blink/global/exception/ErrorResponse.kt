package com.jordyma.blink.global.exception

data class ErrorResponse (
    val code: String,
    val message: String,
    val detail: String?,
) {
    companion object {
        fun of(code: ErrorCode, message: String, detail: String?): ErrorResponse {
            return ErrorResponse(code.name, message, detail)
        }
    }
}