package com.jordyma.blink.message

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MessageRepository : JpaRepository<PushMessage, Long> {

    @Query("select m from PushMessage m where m.date =:now")
    fun findPendingMessages(now: String?): PushMessage?
}