package com.jordyma.blink.folder.service

import com.jordyma.blink.folder.repository.FolderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FolderService @Autowired constructor(
    private val folderRepository: FolderRepository
){
}