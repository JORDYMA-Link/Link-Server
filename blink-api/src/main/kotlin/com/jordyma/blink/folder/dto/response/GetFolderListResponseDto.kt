package com.jordyma.blink.folder.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "폴더 수정 요청 DTO")
data class GetFolderListResponseDto (
    val folderList: List<FolderDto> = emptyList()
)