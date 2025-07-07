package com.jordyma.blink.notice.controller

import com.jordyma.blink.notice.dto.response.NoticeListDto
import com.jordyma.blink.notice.dto.response.WebViewDto
import com.jordyma.blink.notice.service.NoticeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import javax.xml.stream.events.EndDocument

@RestController
@Tag(name = "notice", description = "공지사항 API")
@RequestMapping("/notice")
class NoticeController (
    private val noticeService: NoticeService
        ){

    @Operation(summary = "공지사항 리스트 조회 api", description = "page: 요청할 페이지 번호(0부터 시작), size: 한 페이지에 포함할 데이터 개수")
    @GetMapping("")
    fun getNotices(
        @RequestParam("page") page: Int,
        @RequestParam("size") size: Int,
    ): ResponseEntity<NoticeListDto> {
        val response = NoticeListDto(
            notices = noticeService.getNotices(page, size))
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "웹뷰 조회 api", description = "flag == true일 때 link 띄우기")
    @GetMapping("/webview")
    fun getWebView(): ResponseEntity<WebViewDto> {
        val response = noticeService.getWebView()
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "웹뷰 조회 테스트 api", description = "flag == true일 때 link 띄우기")
    @GetMapping("/webview/test")
    fun getWebViewTest(): ResponseEntity<WebViewDto> {
        val response = noticeService.getWebView()
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "웹뷰 수정 api", description = "link - 웹뷰 url, startDate~endDate - 웹뷰 띄울 기간 설정")
    @PatchMapping("/webview")
    fun updateWebView(
        @RequestParam("link") link: String,
        @RequestParam("startDate") startDate: LocalDate,
        @RequestParam("endDate") endDate: LocalDate,
    ): ResponseEntity<String> {
        noticeService.updateWebView(link, startDate, endDate)
        return ResponseEntity.ok().body("수정이 완료되었습니다.")
    }
}