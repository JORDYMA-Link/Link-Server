package com.jordyma.blink.user.entity

import com.jordyma.blink.global.entity.BaseTimeEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "Member")
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val email: String,
    val socialType: String,
    val socialId: String,
): BaseTimeEntity()
