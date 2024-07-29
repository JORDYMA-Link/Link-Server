package com.jordyma.blink.global.gemini.response

data class PromptResponse(

    val subject: String,

    val summary: String,

    val keywords: List<String>,

    val category: List<String>,
)