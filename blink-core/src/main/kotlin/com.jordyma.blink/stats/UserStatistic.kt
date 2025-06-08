package com.jordyma.blink.stats

import com.jordyma.blink.common.BaseTimeEntity
import jakarta.persistence.*

@Entity
class UserStatistic(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    val type: String,

    var count: Long,

    ) : BaseTimeEntity() {

    fun updateCount(count: Long){
        this.count = count
    }
}