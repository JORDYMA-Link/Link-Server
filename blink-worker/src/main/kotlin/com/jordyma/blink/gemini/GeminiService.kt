package com.jordyma.blink.gemini

import com.jordyma.blink.feed.domain.Feed
import com.jordyma.blink.feed.domain.FeedRepository
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.gemini.request.ChatRequest
import com.jordyma.blink.gemini.response.ChatResponse
import com.jordyma.blink.gemini.response.PromptResponse
import com.jordyma.blink.logger
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate


@Suppress("UNREACHABLE_CODE")
@Service
class GeminiService @Autowired constructor(
    @Qualifier("geminiRestTemplate") private val restTemplate: RestTemplate,
    @Value("\${gemini.api.url}") private val apiUrl: String,
    @Value("\${gemini.api.key}") private val geminiApiKey: String,
    private val feedRepository: FeedRepository,
) {

    fun getContents(link: String, folders: String, userId: Long, content: String, feedId: Long): PromptResponse? {
        // gemini 요청
        val requestUrl = "$apiUrl?key=$geminiApiKey"
        val request = ChatRequest(makePrompt(link, folders, content))
        logger().info("Sending request to Gemini server: $requestUrl with body: $request")

        // gemini 요청값 받아오기
        val response = restTemplate.postForObject(requestUrl, request, ChatResponse::class.java)
        val responseText = response?.candidates?.get(0)?.content?.parts?.get(0)?.text.orEmpty()

        // aiSummary
        logger().info("Received response from Gemini server : ${responseText}")
        val promptResponse = extractJsonAndParse(responseText)
        return promptResponse
    }

    fun makePrompt(link: String, folders: String, content: String): String{
        return "다음 텍스트를 읽고 다음 요구사항을 들어줘.\n" +
                "텍스트 :" + content + "\n" +
                "\t1\t텍스트를 summarize하여 “summary”를 한 문장으로 생성하여 “summary” 부분에 출력해줘.\n" +
                "\t2\t텍스트의 제목을 추출해서 “subject” 부분에 출력해줘.\n" +
                "\t3\t텍스트에 알맞는 keyword를 명사형으로 3개 추출해서 “keyword” 부분에 출력해줘.\n" +
                "\t4\t텍스트에 어울리는 category를 [" + folders + "] 중에 3개 선택해서 “category” : 부분에 출력해줘. 어울리는 category가 없다고 판단되면 새로 생성해줘.\n" +
                "\t5\t출력 형식은 다음과 같이 json 형식으로 출력해줘. { } 안의 내용만 출력해줘. json 형식을 제외한 텍스트는 한 글자도 출력하지 않는다. 추가 설명, 주의사항 등 일절 작성하지 마. 제발 지켜줘\n" +
                "{\n" +
                "\t“subject” : “주제”,\n" +
                "\t“summary” : “요약한 내용”,\n" +
                "\t“keyword” : [“키워드1”, “키워드2”, “키워드3”],\n" +
                "\t“category” : [\"카테고리1\", \"카테고리2\", \"카테고리3\"]\n" +
                "}\n"
    }

    fun extractJsonAndParse(text: String): PromptResponse? {
        // JSON 부분 추출
        val regex = "\\{[^}]*\\}".toRegex()
        val matchResult = regex.find(text)

        // JSON 문자열이 존재하는지 확인
        val jsonString = matchResult?.value

        // JSON 문자열을 ContentData로 파싱하여 반환
        if (jsonString != null) {
            logger().info("jsonString ::: $jsonString")
            val fixedJson = fixQuotes(text)
            logger().info("fixedJson ::: $fixedJson")
            val escapedJson = escapeInternalQuotes(fixedJson)
            logger().info("escapedJson ::: $escapedJson")
            return Json.decodeFromString<PromptResponse>(escapedJson)
        } else {
            logger().info("jsonString is null !!!!!!!!!!")
            return null
        }
    }

    fun fixQuotes(input: String): String {
        return input
            .replace('“', '"')  // 왼쪽 큰 따옴표를 표준 쌍따옴표로 교체
            .replace('”', '"')  // 오른쪽 큰 따옴표를 표준 쌍따옴표로 교체
    }

    fun findFeedOrElseThrow(feedId: Long): Feed {
        return feedRepository.findById(feedId).orElseThrow {
            ApplicationException(ErrorCode.FEED_NOT_FOUND, "피드를 찾을 수 없습니다.")
        }
    }

    fun escapeInternalQuotes(json: String): String {
        val regex = """:\s*"((?:[^"\\]|\\.)*)"""".toRegex()
        return regex.replace(json) { matchResult ->
            val fullMatch = matchResult.value
            val value = matchResult.groups[1]?.value ?: ""
            val escapedValue = value.replace("\"", "“")
            fullMatch.replace(value, escapedValue)
        }
    }
}