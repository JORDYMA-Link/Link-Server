package com.jordyma.blink.folder.repository

import com.jordyma.blink.folder.entity.Folder
import com.jordyma.blink.user.User

interface CustomFolderRepository {

    fun findAllByUser(user: User): List<Folder>

    fun deleteFolder(folder: Folder): Unit
}