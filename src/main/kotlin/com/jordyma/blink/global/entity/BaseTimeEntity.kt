package com.jordyma.blink.global.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import lombok.Getter
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class BaseTimeEntity(
    @CreatedDate
    @Column(name = "createdAt", updatable = false)
    val createdAt: LocalDateTime? = null
) {
    @LastModifiedDate
    @Column(name = "updatedAt")
    var updatedAt: LocalDateTime? = null
        protected set

    @Column(name = "deletedAt")
    var deletedAt: LocalDateTime? = null
        protected set

    fun modifyUpdatedDate(dateTime: LocalDateTime){
        updatedAt = dateTime
    }


    fun modifyDeletedDate(dateTime: LocalDateTime){
        deletedAt = dateTime
    }
}