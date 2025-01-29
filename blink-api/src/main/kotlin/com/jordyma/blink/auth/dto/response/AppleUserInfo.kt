package com.jordyma.blink.auth.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class AppleUserInfo(
    val name: Name,
    val email: String
)

@Serializable
data class Name(
    val firstName: String,
    val lastName: String
)

data class AppleDto(
    val id: String? = null,
    val token: String? = null,
    val email: String? = null
)