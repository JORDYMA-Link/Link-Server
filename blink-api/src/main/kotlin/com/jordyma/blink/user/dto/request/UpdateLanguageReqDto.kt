package com.jordyma.blink.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "유저 언어 설정 수정 요청 DTO")
class UpdateLanguageReqDto (

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