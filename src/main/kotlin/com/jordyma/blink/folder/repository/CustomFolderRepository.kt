package com.jordyma.blink.folder.repository

import com.jordyma.blink.folder.entity.Folder
import com.jordyma.blink.user.entity.User

interface CustomFolderRepository: FolderRepository {

    override fun findAllByUser(user: User): List<Folder>
}