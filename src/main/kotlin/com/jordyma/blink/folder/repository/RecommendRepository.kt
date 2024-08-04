package com.jordyma.blink.folder.repository

import com.jordyma.blink.folder.entity.Recommend
import org.springframework.data.jpa.repository.JpaRepository

interface RecommendRepository : JpaRepository<Recommend, Long> {
}