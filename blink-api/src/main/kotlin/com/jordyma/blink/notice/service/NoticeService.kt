package com.jordyma.blink.notice.service

import com.jordyma.blink.common.system.CommonParameter
import com.jordyma.blink.common.system.CommonParameterRepository
import com.jordyma.blink.notice.dto.response.NoticeResDto
import com.jordyma.blink.notice.NoticeRepository
import com.jordyma.blink.notice.dto.response.WebViewDto
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class NoticeService (
    private val noticeRepository: NoticeRepository,
    private val commonParameterRepository: CommonParameterRepository,
){

    fun getNotices(page: Int, size: Int): List<NoticeResDto> {
        val pageRequest = PageRequest.of(page, size)
        val notices = noticeRepository.findAll(pageRequest)
        return notices.content.map { notice ->
            NoticeResDto(
                date = notice.createdAt.toString(),
                title = notice.title,
                content = notice.content,
            )
        }
    }

    fun getWebView(): WebViewDto {
        val content = commonParameterRepository.findByParamCode(WEBVIEW_CODE)[0]
        val today = LocalDate.now()
        val isWithinValidPeriod = !today.isBefore(content.validStartDate) && !today.isAfter(content.validEndDate)
        return WebViewDto(isWithinValidPeriod, content.paramValue)
    }

    fun getWebViewTest(): WebViewDto {
        val content = commonParameterRepository.findByParamCode(WEBVIEW_TEST_CODE)[0]
        val today = LocalDate.now()
        val isWithinValidPeriod = !today.isBefore(content.validStartDate) && !today.isAfter(content.validEndDate)
        return WebViewDto(isWithinValidPeriod, content.paramValue)
    }

    fun updateWebView(link: String, startDate: LocalDate, endDate: LocalDate) {
        val content = CommonParameter(WEBVIEW_CODE, link, startDate, endDate)
        commonParameterRepository.save(content)
    }

    fun updateWebViewTest(startDate: String, endDate: String) {
        val content = commonParameterRepository.findByParamCode(WEBVIEW_TEST_CODE)[0]
        content.updateStartDate(startDate)
        content.updateEndDate(endDate)
        commonParameterRepository.save(content)
    }

    companion object {
        const val WEBVIEW_CODE = "WEBVIEW_LINK"
        const val WEBVIEW_TEST_CODE = "WEBVIEW_LINK_TEST"
    }

}