package com.jordyma.blink.global.gemini.api

import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.global.gemini.request.ChatRequest
import com.jordyma.blink.global.gemini.response.ChatResponse
import com.jordyma.blink.global.gemini.response.PromptResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import net.minidev.json.JSONObject
//import net.minidev.json.JSONObject
// import org.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate


@Suppress("UNREACHABLE_CODE")
@Service
class GeminiService @Autowired constructor(
    @Qualifier("geminiRestTemplate") private val restTemplate: RestTemplate,
    @Value("\${gemini.api.url}") private val apiUrl: String,
    @Value("\${gemini.api.key}") private val geminiApiKey: String
) {

    fun getContents(link: String, folders: String): PromptResponse? {

        // gemini 요청
        val requestUrl = "$apiUrl?key=$geminiApiKey"
        val request = ChatRequest(makePrompt(link, folders))

        // gemini 요청값 받아오기
        val response = restTemplate.postForObject(requestUrl, request, ChatResponse::class.java)
        val responseText = response?.candidates?.get(0)?.content?.parts?.get(0)?.text.orEmpty()

        // json 파싱
        // val responseJson = extractJsonAndParse(responseText)

        // aiSummary
        if(responseText != null){
            return extractJsonAndParse(responseText)
        }else{
            throw ApplicationException(ErrorCode.JSON_NOT_FOUND, "gemini json 파싱 오류")
        }
    }

    fun makePrompt(link: String, folders: String): String{
        return "이 링크에 있는 텍스트를 읽고 다음 요구사항을 들어줘.\n" +
                "URL :" + link + "\n"
                "\t1\t텍스트를 summarize하여 “summary”를 한 문장으로 생성하여 “summary” 부분에 출력해줘.\n" +
                "\t2\t텍스트의 제목을 추출해서 “subject” 부분에 출력해줘.\n" +
                "\t3\t텍스트에 알맞는 keyword를 명사형으로 3개 추출해서 “keyword” 부분에 출력해줘.\n" +
                "\t4\t텍스트에 어울리는 category를 [" + folders +"] 중에서 3개 선택해서 “category” : 부분에 출력해줘.\n" +
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
        return if (jsonString != null) {
            Json.decodeFromString<PromptResponse>(text)
        } else {
            null
        }
    }

}