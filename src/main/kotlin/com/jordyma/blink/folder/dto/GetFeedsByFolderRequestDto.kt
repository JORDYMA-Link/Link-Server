package com.jordyma.blink.folder.dto

import com.jordyma.blink.feed.dto.FeedDto

class GetFeedsByFolderRequestDto {
    val feedList: List<FeedDto> = mutableListOf<FeedDto>()
}