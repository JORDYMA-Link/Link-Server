package com.jordyma.blink.folder

import com.jordyma.blink.user.User

interface CustomFolderRepository {

    fun findAllByUser(user: User): List<Folder>

    fun deleteFolder(folder: Folder): Unit
}