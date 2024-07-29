package com.jordyma.blink.feed.dto

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.jordyma.blink.global.gemini.response.ChatResponse
import com.jordyma.blink.global.gemini.response.PromptResponse
import io.swagger.v3.oas.annotations.media.Schema
import lombok.NoArgsConstructor
import org.json.JSONObject

@NoArgsConstructor
@Schema(description = "AI 요약 결과 내용 DTO")
data class AiSummaryContent(
    @Schema(description = "제목")
    val subject: String,

    @Schema(description = "요약 내용")
    val summary: String,

    @Schema(description = "키워드")
    val keywords: List<String>,

    @Schema(description = "유저가 생성한 폴더 리스트")
    val folders: List<String>
) {
    companion object {
        fun of(chatResponse: JSONObject, keywords: List<String>, folders: List<String>) = AiSummaryContent(
            subject = chatResponse.optString("subject"),
            summary = chatResponse.optString("summary"),
            keywords = keywords,
            folders = folders
        )
    }
}

@Schema(description = "AI 요약 결과 DTO")
data class AiSummaryResponseDto(
    @Schema(description = "AI 요약 결과 내용")
    val content: AiSummaryContent,

    @Schema(description = "플랫폼 이미지 url")
    val sourceUrl: String,

    @Schema(description = "AI 추천 폴더")
    val recommend: String
){
    companion object {
        fun of(aiSummaryContent: AiSummaryContent, sourceUrl: String, recommend: String) = AiSummaryResponseDto(
            content = aiSummaryContent,
            sourceUrl = sourceUrl,
            recommend = recommend
        )
    }
}
