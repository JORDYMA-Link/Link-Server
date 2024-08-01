package com.jordyma.blink.folder.dto.request

import com.jordyma.blink.feed.dto.FeedDto
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "폴더별 피드 리스트 요청 DTO")
data class GetFeedsByFolderRequestDto (
    @Schema(description = "피드 리스트", type = "array", implementation = FeedDto::class)
    val feedList: List<FeedDto> = mutableListOf<FeedDto>()
)