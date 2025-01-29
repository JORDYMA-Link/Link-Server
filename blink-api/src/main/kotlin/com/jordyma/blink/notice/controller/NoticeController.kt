package com.jordyma.blink.notice.controller

import com.jordyma.blink.notice.dto.response.NoticeListDto
import com.jordyma.blink.notice.service.NoticeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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
}