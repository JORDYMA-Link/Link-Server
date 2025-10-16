package com.jordyma.blink.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "온보딩 유저 정보 입력 요청 DTO")
data class OnboardingUserInfoReqDto(

    @Schema(description = "직무 분야")
    val jobField : String,

    @Schema(description = "연령대")
    val birthYear : String,

    @Schema(description = "성별")
    val gender : String,

    @Schema(description = "언어 ------\n" +
            "한국어: KOREAN,\n" +
            "영어: ENGLISH,\n" +
            "스페인어: SPANISH,\n" +
            "프랑스어: FRENCH,\n" +
            "일본어: JAPANESE,\n" +
            "중국어 간체: SIMPLIFIED_CHINESE,   // 간체\n" +
            "중국어 번체: TRADITIONAL_CHINESE,  // 번체\n" +
            "독일어: GERMAN,")
    val language: String,
)
