package com.jordyma.blink.folder.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "폴더 생성 요청 DTO")
class CreateFolderRequestDto {

    @Schema(description = "폴더 이름")
    val folderName: String = ""
}