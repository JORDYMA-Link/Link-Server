package com.jordyma.blink.folder.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "폴더 수정 요청 DTO")
class UpdateFolderRequestDto {

    @Schema(description = "폴더 이름")
    val name: String = ""
}