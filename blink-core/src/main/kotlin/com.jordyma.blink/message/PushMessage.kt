package com.jordyma.blink.message

import jakarta.persistence.*

@Entity
class PushMessage (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long,

    @Column(name = "title", length = 100)
    val title: String,

    @Column(name = "body", length = 100)
    val body: String,

    @Column(name = "date", length = 20)
    var date: String,
)