package com.jordyma.blink.infra.gemini

import com.jordyma.blink.feed.domain.Feed
import com.jordyma.blink.feed.domain.FeedRepository
import com.jordyma.blink.feed.domain.Status
import com.jordyma.blink.feed.domain.service.ContentSummarizer
import com.jordyma.blink.feed.domain.service.PromptMetadataResponse
import com.jordyma.blink.feed.domain.service.PromptResponse
import com.jordyma.blink.feed.domain.service.PromptSummaryResponse
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

            // 1. summary
            val request = ChatRequest(makeSummarizePrompt(content))
            logger().info("1단계 Summary - Sending request to Gemini server: $requestUrl with body: $request")

            val summaryResponse = restTemplate.postForObject(requestUrl, request, ChatResponse::class.java)
            val summaryResponseText = summaryResponse?.candidates?.get(0)?.content?.parts?.get(0)?.text.orEmpty()
            logger().info("1단계 Summary - Received response from Gemini server: $summaryResponseText")

            val firstResponse = if (summaryResponseText.isNotEmpty()) {
                extractJsonAndParse<PromptSummaryResponse>(summaryResponseText)
            } else {
                throw ApplicationException(ErrorCode.JSON_NOT_FOUND, "summaryResponseText gemini json 파싱 오류")
            }

            // 2. summary를 기반으로 keyword, category 추출
            val metadataRequest = ChatRequest(
                makeMetadataExtractionPrompt(firstResponse.summary, folders)
            )
            logger().info("2단계 Meta정보 추출 - Sending request to Gemini server: $requestUrl with body: $metadataRequest")
            val metadataResponse = restTemplate.postForObject(requestUrl, metadataRequest, ChatResponse::class.java)
            val metadataResponseText = metadataResponse?.candidates?.get(0)?.content?.parts?.get(0)?.text.orEmpty()
            logger().info("2단계 Meta정보 추출 - Received response from Gemini server: $metadataResponseText")

            val secondResponse = if (metadataResponseText.isNotEmpty()) {
                extractJsonAndParse<PromptMetadataResponse>(metadataResponseText)
            } else {
                throw ApplicationException(ErrorCode.JSON_NOT_FOUND, "metadataResponseText gemini json 파싱 오류")
            }

            return PromptResponse(
                subject = firstResponse.subject,
                summary = firstResponse.summary,
                keyword = secondResponse.keyword,
                category = secondResponse.category
            )

        } catch (e: Exception) {
            val feed = findFeedOrElseThrow(feedId)
            feed.updateStatus(Status.FAILED)
            feedRepository.save(feed)

            throw ApplicationException(ErrorCode.JSON_NOT_FOUND, "gemini 요청 처리 중 오류 발생: ${e.message}")
        }
    }

    fun makeSummarizePrompt(content: String, length: Int? = 500): String {
        return """
      다음 텍스트를 읽고 아래 지침에 따라 정확히 요약하고 제목을 추출하세요.
      
      텍스트:
      $content
      
      지침:
      1. 텍스트의 주제를 대표하는 제목을 "subject" 항목에 작성하세요.
      2. 텍스트를 한국어로 요약하고, 그 내용을 "summary" 항목에 작성하세요.
      3. 원문의 흐름과 핵심 정보를 유지하며, 임의로 생략하거나 추가하지 마세요.
      4. 요약 분량은 공백 포함 ${length}자 이내로 작성하세요.
      5. 문장은 반드시 "~습니다"로 끝맺습니다.
      6. 내용 전개에 따라 2~4문장 단위로 문단 구분하며 줄바꿈(\n) 처리하고, 소제목(볼드체)을 지어주세요.
      
      반드시 아래 JSON 형식으로만 응답하세요. 부가 설명은 절대 쓰지 마세요.
      
      {
        "subject": "텍스트 제목",
        "summary": "생성된 요약"
      }
  """.trimIndent()
    }

    fun makeMetadataExtractionPrompt(summary: String, folders: String): String {
        return """
            다음 요약된 텍스트를 읽고 지침에 따라 제목, 키워드, 카테고리를 추출하세요.
            
            요약된 텍스트:
            $summary
            
            추출 지침:
            1. 요약된 내용의 핵심을 나타내는 명사형 키워드를 3개 추출하여 "keyword" 배열에 작성합니다.
            2. 요약된 내용에 가장 적합한 카테고리를 제공된 카테고리 목록 [$folders]에서 3개 선택하여 "category" 배열에 작성합니다.
               - 적절한 것이 없으면 직접 적합한 카테고리를 생성해도 좋습니다.
            
            반드시 아래 JSON 형식으로만 응답하고, 추가 설명이나 부가 정보는 절대 쓰지 마세요.
            
            {
              "keyword": ["키워드1", "키워드2", "키워드3"],
              "category": ["카테고리1", "카테고리2", "카테고리3"]
            }
            
            ❗ JSON 형식 오류가 발생하지 않도록 반드시 쌍따옴표(")를 역슬래시(\)로 이스케이프 처리하세요.
        """.trimIndent()
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

    private inline fun <reified T> extractJsonAndParse(text: String): T {
        // JSON 부분 추출
        val regex = "\\{(?:[^{}]|\\{[^{}]*})*}".toRegex()
        val matchResult = regex.find(text)

        // JSON 문자열이 존재하는지 확인
        val jsonString = matchResult?.value

        // JSON 문자열을 제네릭 타입 T로 파싱하여 반환
        return if (jsonString != null) {
            Json.decodeFromString<T>(jsonString)
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