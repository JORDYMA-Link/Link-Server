package com.jordyma.blink.folder

import com.jordyma.blink.common.BaseTimeEntity
import com.jordyma.blink.user.User
import jakarta.persistence.*

@Entity
class Folder(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @ManyToOne @JoinColumn(name = "user_id")
    val user: User,

    @Column(name = "name", length = 50)
    var name: String,

    @Column(name = "count")
    var count: Int,

    @Column(name = "is_unclassified", columnDefinition = "BIT")
    var isUnclassified: Boolean = false

): BaseTimeEntity() {

    fun changeIsUnclassified(newIsUnclassified: Boolean){
        this.isUnclassified = newIsUnclassified
    }

    fun increaseCount() {
        count++
    }

    fun decreaseCount() {
        count--
    }
}
