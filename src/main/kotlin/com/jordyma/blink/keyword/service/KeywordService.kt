package com.jordyma.blink.keyword.service

import com.jordyma.blink.keyword.repository.KeywordRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class KeywordService(
    private val keywordRepository: KeywordRepository
){
}