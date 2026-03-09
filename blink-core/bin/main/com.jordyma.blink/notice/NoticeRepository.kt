package com.jordyma.blink.notice

import org.springframework.data.jpa.repository.JpaRepository

interface NoticeRepository : JpaRepository<Notice, Long> {
}