package com.jordyma.blink.notice.repository


import com.jordyma.blink.notice.entity.Notice
import org.springframework.data.jpa.repository.JpaRepository

interface NoticeRepository: JpaRepository<Notice, Long> {
}