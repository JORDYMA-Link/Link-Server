package com.jordyma.blink.folder.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "폴더 생성 요청 DTO")
data class CreateFolderRequestDto(

    @Schema(description = "폴더 이름")
    val name: String = ""
)

@Schema(description = "피드에 폴더 생성 요청 DTO")
data class CreateFeedFolderRequestDto(

    @Schema(description = "피드 아이디")
    val feedId: Long,

    @Schema(description = "폴더 이름")
    val name: String = ""
)

@Schema(description = "온보딩 폴더 생성 요청 DTO")
data class OnboardingReqDto(

    @Schema(description = "폴더 이름", example = "[\"개발\", \"동물\"]")
    val topics: List<String>
)