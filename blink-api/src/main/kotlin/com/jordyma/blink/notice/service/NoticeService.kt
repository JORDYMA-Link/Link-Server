package com.jordyma.blink.notice.service

import com.jordyma.blink.notice.dto.response.NoticeResDto
import com.jordyma.blink.notice.NoticeRepository
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service

@Service
class NoticeService (
    private val noticeRepository: NoticeRepository
    ){

    @Transactional
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

}