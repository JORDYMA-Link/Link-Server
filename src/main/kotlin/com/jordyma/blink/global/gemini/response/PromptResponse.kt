package com.jordyma.blink.global.gemini.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class PromptResponse(

    val subject: String,

    val summary: String,

    val keywords: List<String>,

    val category: List<String>,
)