package com.jordyma.blink.global.gemini.response

import kotlinx.serialization.Serializable

@Serializable
data class PromptResponse(

    val subject: String,

    val summary: String,

    val keyword: List<String>,

    val category: List<String>,
)