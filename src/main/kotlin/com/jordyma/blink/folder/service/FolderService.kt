package com.jordyma.blink.folder.service

import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.jordyma.blink.folder.entity.Folder
import com.jordyma.blink.folder.repository.FolderRepository
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.user.entity.User

@Service
@Transactional
@RequiredArgsConstructor
class FolderService (
    private val folderRepository: FolderRepository,
){

    fun create(user: User, topic: String): Folder {
        val folder = Folder(user, topic, 0)
        return folderRepository.save(folder)
    }
}
