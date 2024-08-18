package com.jordyma.blink.global.gemini.api

//import net.minidev.json.JSONObject
// import org.json.JSONObject
import com.jordyma.blink.auth.jwt.user_account.UserAccount
import com.jordyma.blink.feed.service.FeedService
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.global.gemini.request.ChatRequest
import com.jordyma.blink.global.gemini.response.ChatResponse
import com.jordyma.blink.global.gemini.response.PromptResponse
import com.jordyma.blink.logger
import com.jordyma.blink.user.repository.UserRepository
import kotlinx.serialization.json.Json
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.net.URL


@Suppress("UNREACHABLE_CODE")
@Service
class GeminiService @Autowired constructor(
    @Qualifier("geminiRestTemplate") private val restTemplate: RestTemplate,
    @Value("\${gemini.api.url}") private val apiUrl: String,
    @Value("\${gemini.api.key}") private val geminiApiKey: String,
    private val feedService: FeedService,
    private val userRepository: UserRepository,
) {

    fun getContents(link: String, folders: String, userAccount: UserAccount, content: String): PromptResponse? {
        return try {

//            // 지정된 URL로 접속하여 HTML 문서를 가져옴
//            val document = Jsoup.connect(link).get()
//
//            // 페이지의 본문에 해당하는 'content' 클래스를 가진 <div> 요소를 선택
//            val htmlText = document.select("div.content")
//
//            // 본문 내용을 텍스트로 추출
//            val htmlContent = htmlText.text()
//
//            logger().info("htmlParser ::::::: $htmlContent")

//            val htmlFetcher = HtmlFetcher()
//            val htmlContent = htmlFetcher.fetchHtml(link)
//            c

            // 지정된 URL로 접속하여 HTML 문서를 가져옴
//            val document = Jsoup.connect(link).get()
//
//            // 모든 <div> 태그를 선택
//            val divs = document.select("div")
//
//            // 각 <div> 태그의 텍스트를 하나의 문자열로 이어붙임
//            val htmlContent = divs.joinToString(separator = "\n") { it.text() }

            // val htmlContent = getHtml(link)
            // logger().info("htmlParser ::::::: $htmlContent")

            // gemini 요청
            val requestUrl = "$apiUrl?key=$geminiApiKey"
            val request = ChatRequest(makePrompt(link, folders, content))
            logger().info("Sending request to Gemini server: $requestUrl with body: $request")

            // gemini 요청값 받아오기
            val response = restTemplate.postForObject(requestUrl, request, ChatResponse::class.java)
            val responseText = response?.candidates?.get(0)?.content?.parts?.get(0)?.text.orEmpty()
            logger().info("Received response from Gemini server: $response")


            // aiSummary
            if (responseText.isNotEmpty()) {
                extractJsonAndParse(responseText)

            } else {
                throw ApplicationException(ErrorCode.JSON_NOT_FOUND, "gemini json 파싱 오류")
            }
        } catch (e: Exception) {
            // 요약 실패 feed 생성
            feedService.createFailed(userAccount, link)
            throw ApplicationException(ErrorCode.JSON_NOT_FOUND, "gemini 요청 처리 중 오류 발생: ${e.message}")
        }
    }

//    fun getHtml(link: String): String {
//        var returnVal = ""
//        try {
//            //val originalUrl = "https://bbs.ruliweb.com/community/board/300143"
//            val originalDoc: Document = Jsoup.connect(link).get()
//            val linkElements: Elements = originalDoc.select("a.deco[href*=read]")
//            for (linkElement in linkElements) {
//                val linkUrl: String = linkElement.attr("href")
//                val absoluteUrl = URL(URL(link), linkUrl)
//                val absoluteUrlString: String = absoluteUrl.toString()
//                val newDoc: Document = Jsoup.connect(absoluteUrlString).get()
//                val elements: Elements = newDoc.select("article > div > p")
//                println("\"$absoluteUrlString\" :해당 게시물 div tag: ")
//                val contentBuilder = StringBuilder()
//                // 해당 링크 text부분 긁어오기
//                for (element in elements) {
//                    contentBuilder.append(element.text())
//                    contentBuilder.append("\n") // <br> 태그를 만날 때마다 줄바꿈 추가
//                }
//                println(contentBuilder.toString())
//                returnVal = contentBuilder.toString()
//            }
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }
//        return returnVal
//    }

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
        return if (jsonString != null) {
            logger().info("jsonString ::: $jsonString")
            val fixedJson = fixQuotes(text)
            logger().info("fixedJson ::: $fixedJson")
            Json.decodeFromString<PromptResponse>(fixedJson)
        } else {
            logger().info("jsonString is null !!!!!!!!!!")
            null
        }
    }

    fun fixQuotes(input: String): String {
        return input
            .replace('“', '"')  // 왼쪽 큰 따옴표를 표준 쌍따옴표로 교체
            .replace('”', '"')  // 오른쪽 큰 따옴표를 표준 쌍따옴표로 교체
    }
}