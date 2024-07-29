package com.jordyma.blink.folder.service

import com.jordyma.blink.folder.entity.Folder
import com.jordyma.blink.folder.repository.FolderRepository
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.user.entity.User
import com.jordyma.blink.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FolderService(
    private val folderRepository: FolderRepository,
    private val userRepository: UserRepository,
) {
    fun getFolderNames(userId: Long): List<String> {
        val user = findUserOrThrow(userId)
        return folderRepository.findByUser(user).stream().map { folder -> folder.name }.toList();
    }

    private fun findUserOrThrow(userId: Long): User {
        return userRepository.findById(userId).orElseThrow {
            ApplicationException(ErrorCode.NOT_FOUND, "존재하지 않는 id입니다 : $userId")
        }
    }
}