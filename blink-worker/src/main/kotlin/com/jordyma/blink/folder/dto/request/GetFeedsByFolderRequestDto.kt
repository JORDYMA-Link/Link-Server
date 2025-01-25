package com.jordyma.blink.folder.dto.request

import io.swagger.v3.oas.annotations.media.Schema

data class GetFeedsByFolderRequestDto(
    @Schema(description = "무한 페이징 커서 (feed ID)", example = "1")
    val cursor: Int?,

    @Schema(description = "페이지 크기", example = "10", defaultValue = "10")
    val pageSize: Long = 10,
)