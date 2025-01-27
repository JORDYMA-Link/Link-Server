package com.jordyma.blink.gemini.request

data class ChatRequest(
    var contents: List<Content> = mutableListOf(),
    var generationConfig: GenerationConfig = GenerationConfig()
) {
    constructor(prompt: String) : this() {
        val parts = Parts(text = prompt)
        val content = Content(parts = parts)
        this.contents = listOf(content)
        this.generationConfig = GenerationConfig(
            candidateCount = 1,
            maxOutputTokens = 1000,
            temperature = 0.7
        )
    }

    data class Content(
        var parts: Parts = Parts()
    )

    data class Parts(
        var text: String = ""
    )

    data class GenerationConfig(
        var candidateCount: Int = 1,
        var maxOutputTokens: Int = 1000,
        var temperature: Double = 0.7
    )
}
