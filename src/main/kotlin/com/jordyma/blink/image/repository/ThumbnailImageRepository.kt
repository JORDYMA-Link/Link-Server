package com.jordyma.blink.image.repository

import com.jordyma.blink.image.entity.thumbnail.ThumbnailImage
import org.springframework.data.jpa.repository.JpaRepository

interface ThumbnailImageRepository: JpaRepository<ThumbnailImage, Long> {
}