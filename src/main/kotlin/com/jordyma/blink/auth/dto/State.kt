package com.jordyma.blink.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class State (
    val webRedirectUrl: String,
)