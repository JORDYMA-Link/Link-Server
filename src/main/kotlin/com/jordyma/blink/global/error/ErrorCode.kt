package com.jordyma.blink.global.error


data class ErrorCode(
    val code: String,
    val message: String
)

val USER_NOT_FOUND = ErrorCode("M1", "해당 사용자를 찾을 수 없습니다")
val FOLDER_NOT_FOUND = ErrorCode("M1", "해당 폴더를 찾을 수 없습니다")
val FEEDS_NOT_FOUND = ErrorCode("M1", "해당 피드를 찾을 수 없습니다")
val KEYWORDS_NOT_FOUND = ErrorCode("M1", "해당 키워드를 찾을 수 없습니다")

val ID_NOT_FOUND = ErrorCode("M2", "ID should not be null")