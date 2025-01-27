package com.jordyma.blink.feed

enum class Status{

    REQUESTED,   // 요약 요청
    PROCESSING,  // 요약 중
    COMPLETED,   // 요약 완료
    SAVED,       // 유저 저장 완료
    FAILED;      // 요약 실패
}