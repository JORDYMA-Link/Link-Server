package com.jordyma.blink.stats

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface UserStatisticRepository : JpaRepository<UserStatistic, Long> {

    @Query("SELECT u FROM UserStatistic u WHERE u.type = :type AND DATE(u.date) = DATE(:date)")
    fun findByTypeAndDate(@Param("type") type: String, @Param("date") date: LocalDateTime): UserStatistic?
}