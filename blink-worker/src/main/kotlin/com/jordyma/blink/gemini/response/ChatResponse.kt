package com.jordyma.blink.gemini.response

data class ChatResponse(
    var candidates: List<Candidate> = mutableListOf(),
    var promptFeedback: PromptFeedback? = null
) {
    data class Candidate(
        var content: Content? = null,
        var finishReason: String? = null,
        var index: Int = 0,
        var safetyRatings: List<SafetyRating> = mutableListOf()
    )

    data class Content(
        var parts: List<Parts> = mutableListOf(),
        var role: String? = null
    )

    data class Parts(
        var text: String? = null
    )

    data class SafetyRating(
        var category: String? = null,
        var probability: String? = null
    )

    data class PromptFeedback(
        var safetyRatings: List<SafetyRating> = mutableListOf()
    )
}
