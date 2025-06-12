package com.jordyma.blink.stats

import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface UserStatisticRepository : JpaRepository<UserStatistic, Long> {

    @Query("SELECT u FROM UserStatistic u WHERE u.type = :type AND DATE(u.createdAt) = DATE(:date)")
    fun findByTypeAndDate(@Param("type") type: String, @Param("date") date: LocalDateTime): UserStatistic?
}