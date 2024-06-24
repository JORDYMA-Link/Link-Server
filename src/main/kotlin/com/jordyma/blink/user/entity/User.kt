package com.jordyma.blink.user.entity

import com.jordyma.blink.global.entity.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "Member")
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(length = 50)
    val email: String,

    @Column(length = 50)
    val socialType: String,

    @Column(length = 50)
    val socialId: String,
): BaseTimeEntity()
