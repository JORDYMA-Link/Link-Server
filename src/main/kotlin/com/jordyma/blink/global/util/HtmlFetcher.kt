package com.jordyma.blink.global.util

import org.jsoup.Jsoup
import org.springframework.stereotype.Service


@Service
class HtmlFetcher {

    fun fetchHtml(url: String): String {
        val document = Jsoup.connect(url).get()
        return document.html()  // 전체 HTML을 가져옴
    }

    fun fetchTitle(url: String): String {
        val document = Jsoup.connect(url).get()
        return document.title()  // HTML에서 <title> 태그의 내용을 가져옴
    }
}