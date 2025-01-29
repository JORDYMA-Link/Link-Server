package com.jordyma.blink.feed.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "요약 중인 링크 (요약 완료, 요약 중, 요약 실패) dto")
data class ProcessingFeedResDto(
    val feedId: Long,
    val title: String,
    val status: String,
)

@Schema(description = "요약 중인 링크 리스트 response dto")
data class ProcessingListDto(
    val processingFeedResDtos: List<ProcessingFeedResDto>,
)