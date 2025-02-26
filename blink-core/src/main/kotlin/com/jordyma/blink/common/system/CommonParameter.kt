package com.jordyma.blink.common.system

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(
    name = "common_parameter",
    uniqueConstraints = [UniqueConstraint(columnNames = ["param_code", "param_value"])]
)
class CommonParameter(
    @Column(name = "param_code", length = 50, nullable = false)
    val paramCode: String,

    @Column(name = "param_value", nullable = false)
    val paramValue: String,

    @Column(name = "valid_start_date")
    val validStartDate: LocalDate? = null,

    @Column(name = "valid_end_date")
    val validEndDate: LocalDate? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    protected constructor() : this("", "") // JPA 기본 생성자를 위해
}