package com.jordyma.blink.feed_summarizer.html_parser

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.safety.Safelist
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class HtmlParserV2 {

    data class PageInfo(
        val title: String,
        val content: String,
        val thumbnailImage: String,
    )

    fun parseUrl(url: String): PageInfo {
        val document = when {
            url.contains(NAVER_BLOG_BASE_URL) -> fetchNaverBlogContent(url)
            else -> fetchContent(url)
        }

        return PageInfo(
            title = document.title(),
            content = extractContent(document),
            thumbnailImage = extractThumbnailImage(document)
        )
    }

    private fun fetchNaverBlogContent(url: String): Document {
        val mainDoc = createJsoupConnection(url).get()
        val iframeSrc = mainDoc.select("iframe#mainFrame").attr("src")
        val realUrl = NAVER_BLOG_BASE_URL + iframeSrc
        return createJsoupConnection(realUrl).get()
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