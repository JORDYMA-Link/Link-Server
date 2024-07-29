package com.jordyma.blink.folder.dto.request

data class FolderCreateReqDto(
    val topic: String,
    val userId:Long,
)

data class OnboardingReqDto(
    val topics: List<String>,
    val userId: Long
)