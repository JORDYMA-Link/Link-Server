package com.jordyma.blink.folder.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "폴더 DTO")
data class FolderDto (
    @Schema(description = "폴더 ID")
    val id: Long?,

    @Schema(description = "폴더 이름")
    val name: String,

    @Schema(description = "피드 개수")
    val feedCount: Int,
)