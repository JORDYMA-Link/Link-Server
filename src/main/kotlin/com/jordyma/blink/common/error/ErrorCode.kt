package com.jordyma.blink.common.error


data class ErrorCode(
    val code: String,
    val message: String
)

val USER_NOT_FOUND = ErrorCode("M1", "해당 사용자를 찾을 수 없습니다")