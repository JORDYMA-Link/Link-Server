package com.jordyma.blink.user.dto

import com.jordyma.blink.user.LanguageType

data class UserInfoDto(
    val id: Long,
    val name: String,
    val language: LanguageType? = LanguageType.KOREAN,
)