package com.jordyma.blink.stats

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "user_statistic")
class UserStatistic(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "type", nullable = false)
    val type: String,

    @Column(name = "count", nullable = false)
    var count: Long,

    @Column(name = "date", nullable = false)
    var date: LocalDateTime,
) {
    fun updateCount(newCount: Long) {
        this.count = newCount
    }

    override fun toString(): String {
        return "UserStatistic(id=$id, type='$type', count=$count, date=$date)"
    }
}