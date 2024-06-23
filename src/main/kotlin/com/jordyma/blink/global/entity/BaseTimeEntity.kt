package com.jordyma.blink.global.entity

import jakarta.persistence.*
import lombok.Getter
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class BaseTimeEntity {

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    var createdAt: LocalDateTime? = null
        private set

    @LastModifiedDate
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    var updatedAt: LocalDateTime? = null
        private set

    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    var deletedAt: LocalDateTime? = null
        private set
}