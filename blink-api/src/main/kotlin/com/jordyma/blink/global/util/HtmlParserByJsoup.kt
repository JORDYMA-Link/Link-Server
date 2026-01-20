package com.jordyma.blink.global.util

import com.jordyma.blink.logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.safety.Safelist
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class HtmlParserByJsoup(
    private val seleniumPageParser: SeleniumPageParser
) {

    // Selenium 전용 제한된 Dispatcher (최대 5개 병렬 실행)
    private val seleniumDispatcher = Dispatchers.IO.limitedParallelism(5)

    data class PageInfo(
        val title: String,
        val content: String,
        val thumbnailImage: String,
    )


    fun parseUrl(url: String): PageInfo {
        val document = when {
            url.contains(NAVER_BLOG_BASE_URL) -> fetchNaverBlogContent(url)
            url.contains("naver.me") -> fetchNaverShortUrl(url)
            else -> fetchContent(url)
        }

        return PageInfo(
            title = document.title(),
            content = extractContent(document),
            thumbnailImage = extractThumbnailImage(document)
        )
    }

    suspend fun parseUrlAsync(url: String): PageInfo = withContext(Dispatchers.IO) {
        logger().info(">>>>> [HtmlParserByJsoup] Starting Jsoup parsing for: $url")

        // 1단계: Jsoup으로 빠르게 파싱 시도
        val document = when {
            url.contains(NAVER_BLOG_BASE_URL) -> fetchNaverBlogContent(url)
            url.contains("naver.me") -> fetchNaverShortUrl(url)
            else -> fetchContent(url)
        }

        val jsoupResult = PageInfo(
            title = document.title(),
            content = extractContent(document),
            thumbnailImage = extractThumbnailImage(document)
        )

        // 2단계: 빈 콘텐츠 감지
        if (isEmptyContent(jsoupResult.content)) {
            logger().info(">>>>> [HtmlParserByJsoup] Empty content detected, falling back to Selenium for: $url")

            // 3단계: Selenium으로 fallback (제한된 스레드풀에서 실행)
            return@withContext withContext(seleniumDispatcher) {
                try {
                    seleniumPageParser.parseUrl(url)
                } catch (e: Exception) {
                    logger().error(">>>>> [HtmlParserByJsoup] Selenium fallback failed: ${e.message}", e)
                    // Selenium도 실패하면 Jsoup 결과 반환
                    jsoupResult
                }
            }
        }

        logger().info(">>>>> [HtmlParserByJsoup] Jsoup parsing succeeded. Content length: ${jsoupResult.content.length}")
        return@withContext jsoupResult
    }

    /**
     * 빈 콘텐츠 감지 함수
     * - 콘텐츠가 비어있거나 50자 미만이면 빈 것으로 간주
     */
    private fun isEmptyContent(content: String): Boolean {
        val trimmedContent = content.trim()
        return trimmedContent.isEmpty() || trimmedContent.length < 50
    }


    private fun fetchNaverBlogContent(url: String): Document {
        val mainDoc = createJsoupConnection(url).get()
        val iframeSrc = mainDoc.select("iframe#mainFrame").attr("src")
        val realUrl = NAVER_BLOG_BASE_URL + iframeSrc
        return createJsoupConnection(realUrl).get()
    }


    private fun fetchNaverShortUrl(url: String): Document {
        val response = createJsoupConnection(url).execute()
        val redirectedUrl = response.url().toString()
        logger().info("리디렉트된 URL: $redirectedUrl")

        // 만약 link.naver.com/bridge로 리디렉트 됐다면, url 파라미터 값 추출
        val finalUrl = if (redirectedUrl.contains("link.naver.com/bridge")) {
            extractUrlFromBridge(redirectedUrl)
        } else {
            redirectedUrl
        }

        println("최종 URL: $finalUrl")

        // 리디렉트된 실제 URL로 다시 요청
        return createJsoupConnection(finalUrl).get()
    }


    private fun fetchContent(url: String): Document =
        createJsoupConnection(url).get()


    private fun extractContent(document: Document): String {
        val content = when {
            document.select(".se-main-container").isNotEmpty() ->
                document.select(".se-main-container").text()
            document.select("body").hasText() ->
                document.select("body").text()
            else -> document.html()
        }
        return cleanHtml(content)
    }


    private fun extractThumbnailImage(document: Document): String =
        document.select("meta[property=og:image]")
            .firstOrNull()
            ?.attr("content")
            ?: ""


    private fun extractUrlFromBridge(redirectedUrl: String): String {
        val uri = java.net.URI(redirectedUrl)
        val queryParams = uri.query.split("&").associate {
            val (key, value) = it.split("=")
            key to java.net.URLDecoder.decode(value, "UTF-8")
        }

        return queryParams["url"] ?: redirectedUrl
    }


    private fun createJsoupConnection(url: String) = Jsoup.connect(url)
        .timeout(Duration.ofSeconds(TIMEOUT_SECONDS.toLong()).toMillis().toInt())
        .userAgent(USER_AGENT)
        .followRedirects(true)


    private fun cleanHtml(html: String): String =
        Jsoup.clean(html, Safelist.relaxed())


    companion object {
        private const val NAVER_BLOG_BASE_URL = "https://blog.naver.com"
        private const val TIMEOUT_SECONDS = 40
        private const val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36"
    }
}