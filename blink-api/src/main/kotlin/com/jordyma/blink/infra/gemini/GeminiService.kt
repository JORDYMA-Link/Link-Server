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
        다음 텍스트를 읽고 다음 요구사항을 들어줘.
        텍스트 : {$content}
        1. 텍스트를 summarize하여 “summary”를 생성하되, 다음 [summary 작성 규칙]을 반드시 준수합니다.
            [summary 작성 규칙]
            - 목적: 사용자가 10초 이내에 글의 주장·핵심 논거·주요 데이터를 파악하도록 구조화하고, 마지막에 3줄 요약으로 랩업합니다.
            - 시점 규칙 (POV): 항상 3인칭 해요체를 사용하며, 주어를 명시합니다(예: “글쓴이는…”, “사용자는…”). 경험·감정·의견은 “…라고 말해요/설명해요”와 같이 간접 인용으로 처리합니다. 무주어 문장과 1인칭 어휘 사용을 금지하고, 관찰자 시선을 유지합니다.
            - 요약 문단: 글 전체 요지를 3~4문장으로 압축하여 서론 없이 중요한 내용부터 시작합니다. 구체적 사건·행동·상황을 사용하고, 한 문장에 1~2개 사건만 포함합니다.
            - 핵심 포인트: 논리 구조를 계층화하여 내용을 주제별로 분류합니다. 최상위(H2) 소제목 아래에 하위(H3/H4) 내용을 간결하게 정리합니다. 불필요한 부연·사례를 제외하고, 주어·동사를 문장 앞에 둡니다.
            - 3줄 요약: 가장 중요한 3가지 포인트를 15~25자 내외로 요약합니다. 3인칭 해요체를 사용하고, 원인 → 결과 구조를 선호하며, 긴 문장은 나누어 리듬감을 유지합니다.
            - 타이포그래피 규칙:
                * H1: # 텍스트 → 글 전체 제목
                * H2: ## 텍스트 → 핵심 포인트(대항목)
                * H3: ### 텍스트 → 핵심 포인트(소항목)
                * H4: #### 텍스트 → 세부 내용
                * 목록: 불릿(• 또는 -) 사용
                * 문단 간 1줄 띄우기
            - 산출 직전 POV 체크리스트:
                * 모든 문단이 명시적 3인칭 주어로 시작했는가?
                * 경험·감정·평가가 간접 인용으로 처리되었는가?
                * 1인칭 어휘나 무주어 문장이 없는가?
                * 해요체이면서 관찰자 시선을 유지했는가?
        2. 텍스트의 제목을 추출해서 “subject” 부분에 출력해줘.
        3. 반드시 아래 JSON 형식으로만 응답하세요. 부가 설명은 절대 쓰지 마세요.
        {
            "subject" : "주제",
            "summary" : "요약한 내용"
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
        다음 텍스트를 읽고 다음 요구사항을 들어줘.
        텍스트 : {$content}
        1. 텍스트를 summarize하여 “summary”를 생성하되, 다음 [summary 작성 규칙]을 반드시 준수합니다.
            [summary 작성 규칙]
            - 목적: 사용자가 10초 이내에 글의 주장·핵심 논거·주요 데이터를 파악하도록 구조화하고, 마지막에 3줄 요약으로 랩업합니다.
            - 시점 규칙 (POV): 항상 3인칭 해요체를 사용하며, 주어를 명시합니다(예: “글쓴이는…”, “사용자는…”). 경험·감정·의견은 “…라고 말해요/설명해요”와 같이 간접 인용으로 처리합니다. 무주어 문장과 1인칭 어휘 사용을 금지하고, 관찰자 시선을 유지합니다.
            - 요약 문단: 글 전체 요지를 3~4문장으로 압축하여 서론 없이 중요한 내용부터 시작합니다. 구체적 사건·행동·상황을 사용하고, 한 문장에 1~2개 사건만 포함합니다.
            - 핵심 포인트: 논리 구조를 계층화하여 내용을 주제별로 분류합니다. 최상위(H2) 소제목 아래에 하위(H3/H4) 내용을 간결하게 정리합니다. 불필요한 부연·사례를 제외하고, 주어·동사를 문장 앞에 둡니다.
            - 3줄 요약: 가장 중요한 3가지 포인트를 15~25자 내외로 요약합니다. 3인칭 해요체를 사용하고, 원인 → 결과 구조를 선호하며, 긴 문장은 나누어 리듬감을 유지합니다.
            - 타이포그래피 규칙:
                * H1: # 텍스트 → 글 전체 제목
                * H2: ## 텍스트 → 핵심 포인트(대항목)
                * H3: ### 텍스트 → 핵심 포인트(소항목)
                * H4: #### 텍스트 → 세부 내용
                * 목록: 불릿(• 또는 -) 사용
                * 문단 간 1줄 띄우기
            - 산출 직전 POV 체크리스트:
                * 모든 문단이 명시적 3인칭 주어로 시작했는가?
                * 경험·감정·평가가 간접 인용으로 처리되었는가?
                * 1인칭 어휘나 무주어 문장이 없는가?
                * 해요체이면서 관찰자 시선을 유지했는가?
        2. 텍스트의 제목을 추출해서 “subject” 부분에 출력해줘.
        3. 텍스트에 알맞는 keyword를 명사형으로 3개 추출해서 “keyword” 부분에 출력해줘.
        4. 텍스트에 어울리는 category를 [{$folders}] 중에 3개 선택해서 “category” : 부분에 출력해줘. 어울리는 category가 없다고 판단되면 새로 생성해줘.
        5. 출력 형식은 다음과 같이 JSON 형식으로 출력해줘. { } 안의 내용만 출력해줘. JSON 형식을 제외한 텍스트는 한 글자도 출력하지 않는다. 추가 설명, 주의사항 등은 작성하지 마.
        {
            "subject" : "주제",
            "summary" : "요약한 내용",
            "keyword" : ["키워드1", "키워드2", "키워드3"],
            "category" : ["카테고리1", "카테고리2", "카테고리3"]
        }
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