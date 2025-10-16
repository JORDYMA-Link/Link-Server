package com.jordyma.blink.user

import com.jordyma.blink.common.BaseTimeEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "user")
class User(
    @Column(name = "nickname")
    var nickname: String = "",

    @Column(name = "socialType")
    @Enumerated(EnumType.STRING)
    val socialType: SocialType? = null,

    @Column(name = "socialUserId")
    var socialUserId: String? = null,

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    var role: Role? = null,

    @Column(name = "iosPushToken")
    var iosPushToken: String? = null,

    @Column(name = "aosPushToken")
    var aosPushToken: String? = null,

    @Column(name = "gender")
    var gender: String? = null,

    @Column(name = "jobField")
    var jobField: String? = null,

    @Column(name = "birthYear")
    var birthYear: String? = null,

    @Column(name = "language")
    @Enumerated(EnumType.STRING)
    var language: LanguageType? = null,

): BaseTimeEntity()  {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null

    fun updateNickname(nickname: String) {
        this.nickname = nickname
    }

    fun updateDeletedAt(){
        this.deletedAt = LocalDateTime.now()
    }

    fun updateSocialId(){
        this.socialUserId = this.socialUserId + "deleted"
    }

    fun updateJobField(jobField: String) {
        this.jobField = jobField
    }

    fun updateGender(gender: String) {
        this.gender = gender
    }

    fun updateBirthYear(birthYear: String) {
        this.birthYear = birthYear
    }

    fun updateLanguage(language: String) {
        this.language = LanguageType.valueOf(language);
    }
}