package com.jordyma.blink.infra.gemini

import com.jordyma.blink.feed.domain.Feed
import com.jordyma.blink.feed.domain.FeedRepository
import com.jordyma.blink.feed.domain.Status
import com.jordyma.blink.feed.domain.service.ContentSummarizer
import com.jordyma.blink.feed.domain.service.PromptResponse
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.infra.gemini.request.ChatRequest
import com.jordyma.blink.infra.gemini.response.ChatResponse
import com.jordyma.blink.logger
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class GeminiService @Autowired constructor(
    @Qualifier("geminiRestTemplate") private val restTemplate: RestTemplate,
    @Value("\${gemini.api.url}") private val apiUrl: String,
    @Value("\${gemini.api.key}") private val geminiApiKey: String,
    private val feedRepository: FeedRepository
) : ContentSummarizer {

    override fun summarize(
        content: String,
        link: String,
        folders: String,
        userId: Long,
        feedId: Long
    ): PromptResponse {
        try {
            val requestUrl = "$apiUrl?key=$geminiApiKey"
            val request = ChatRequest(makePrompt(link, folders, content))
            logger().info("Sending request to Gemini server: $requestUrl with body: $request")

            val response = restTemplate.postForObject(requestUrl, request, ChatResponse::class.java)
            val responseText = response?.candidates?.get(0)?.content?.parts?.get(0)?.text.orEmpty()
            logger().info("Received response from Gemini server: $response")

            if (responseText.isNotEmpty()) {
                return extractJsonAndParse(responseText)
            } else {
                throw ApplicationException(ErrorCode.JSON_NOT_FOUND, "gemini json 파싱 오류")
            }
        } catch (e: Exception) {
            val feed = findFeedOrElseThrow(feedId)
            feed.updateStatus(Status.FAILED)
            feedRepository.save(feed)

            throw ApplicationException(ErrorCode.JSON_NOT_FOUND, "gemini 요청 처리 중 오류 발생: ${e.message}")
        }
    }

    fun makePrompt(link: String, folders: String, content: String): String{
        return """
        다음 텍스트를 읽고 아래 요구사항을 충족하는 JSON을 생성해줘.
        
        텍스트: 
        $content
        
        요구사항:
        1. 텍스트를 summarize해서 "summary"에 한 문장으로 작성
        2. 텍스트의 제목을 추출해서 "subject"에 작성
        3. 텍스트에서 명사형 키워드 3개를 추출해 "keyword" 배열에 작성
        4. 텍스트에 어울리는 category를 [${folders}] 중에서 3개 선택해서 "category" 배열에 작성. 적절한 게 없으면 새로 생성해도 됨.
        
        반드시 유효한 JSON으로만 응답해. JSON 형식은 아래와 같아.
        
        {
            "subject": "텍스트 제목",
            "summary": "텍스트 요약",
            "keyword": ["키워드1", "키워드2", "키워드3"],
            "category": ["카테고리1", "카테고리2", "카테고리3"]
        }
        
        ❗ 반드시 위 JSON 구조만 출력하고, 추가 설명이나 부가 정보는 절대 쓰지 마.
        ❗ JSON 포맷이 틀리면 시스템이 에러 처리하니 정확히 작성해줘.
        ❗ 출력값 안에 쌍따옴표(", “, ”)가 있을 경우 반드시 역슬래시(\)를 사용해 이스케이프 처리해줘.
    """.trimIndent()

    }

    fun extractJsonAndParse(text: String): PromptResponse {
        // JSON 부분 추출
        val regex = "\\{[^}]*\\}".toRegex()
        val matchResult = regex.find(text)

        // JSON 문자열이 존재하는지 확인
        val jsonString = matchResult?.value

        // JSON 문자열을 ContentData로 파싱하여 반환
        return if (jsonString != null) {
            //val fixedJson = fixQuotes(jsonString)
            Json.decodeFromString<PromptResponse>(jsonString)
        } else {
            throw ApplicationException(ErrorCode.JSON_PARSING_FAILED, "gemini json 파싱 실패")
        }
    }

    fun fixQuotes(input: String): String {
        return input
            .replace('“', '\'')
            .replace('”', '\'')
    }

    fun findFeedOrElseThrow(feedId: Long): Feed {
        return feedRepository.findById(feedId).orElseThrow {
            ApplicationException(ErrorCode.FEED_NOT_FOUND, "피드를 찾을 수 없습니다.")
        }
    }
}