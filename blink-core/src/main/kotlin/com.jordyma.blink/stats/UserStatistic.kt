package com.jordyma.blink.stats

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class UserStatistic(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    val type: String,

    var count: Long,

    var date: LocalDateTime,

    ) {

    fun updateCount(count: Long){
        this.count = count
    }
}