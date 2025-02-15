package com.jordyma.blink.folder.domain.service

import com.jordyma.blink.folder.Folder
import com.jordyma.blink.folder.domain.model.FolderDto
import com.jordyma.blink.folder.domain.model.GetFeedsByFolderRequestDto
import com.jordyma.blink.folder.domain.model.GetFeedsByFolderResponseDto

interface FolderService {

    fun getFolders(userId: Long): List<FolderDto>

    fun delete(userId: Long, folderId: Long)

    fun signOutDelete(userId: Long)

    fun getFeedsByFolder(userId: Long, folderId: Long, request: GetFeedsByFolderRequestDto): GetFeedsByFolderResponseDto

    fun create(userId: Long, folderName: String): FolderDto

    fun update(userId: Long, folderId: Long, folderName: String): FolderDto

    fun createFeedFolder(userId: Long, feedId: Long, folderName: String): FolderDto?

    fun getUnclassified(userId: Long): Folder

    fun getFailed(userId: Long): Folder?

    fun getFolderById(folderId: Long): Folder
}