package com.jordyma.blink.folder.service

import com.jordyma.blink.auth.jwt.user_account.UserAccount
import com.jordyma.blink.feed.dto.FeedCalendarResponseDto
import com.jordyma.blink.folder.dto.CreateFolderRequestDto
import com.jordyma.blink.folder.dto.FolderDto
import com.jordyma.blink.folder.dto.GetFolderListResponseDto
import com.jordyma.blink.folder.dto.UpdateFolderRequestDto
import com.jordyma.blink.folder.repository.FolderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FolderService(
    private val folderRepository: FolderRepository
) {
    fun getFolders(user: UserAccount): GetFolderListResponseDto {
        TODO("Not yet implemented")
    }

    fun delete(userAccount: UserAccount, folderId: Long) {
        TODO("Not yet implemented")
    }

    fun getFeedsByFolder(userAccount: UserAccount, folderId: Long): FeedCalendarResponseDto? {
        TODO("Not yet implemented")
    }

    fun create(userAccount: UserAccount, requestDto: CreateFolderRequestDto): FolderDto {
        TODO("Not yet implemented")
    }

    fun update(userAccount: UserAccount, folderId: Long, folderName: String, requestDto: UpdateFolderRequestDto) {
        TODO("Not yet implemented")
    }

    fun update(userAccount: UserAccount, folderId: Long, requestDto: UpdateFolderRequestDto): FolderDto {
        TODO("Not yet implemented")
    }
}