package com.jordyma.blink.global.util

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.safety.Safelist
import org.springframework.stereotype.Service


@Service
class HtmlParserService {

    fun fetchHtmlContent(url: String): String {
        val document: Document = Jsoup.connect(url).get()
        var content = document.select("body").html()  // 전체 HTML을 가져옴
        content = cleanHtml(content)

        return content
    }


    fun fetchTitle(url: String): String {
        val document = Jsoup.connect(url).get()
        return document.title()  // HTML에서 <title> 태그의 내용을 가져옴
    }

    private fun cleanHtml(html: String): String {
        // 기본적으로 텍스트 포맷팅에 필요한 몇 가지 태그만 허용해서 html 정제
        return Jsoup.clean(html, Safelist.basic())
    }
}