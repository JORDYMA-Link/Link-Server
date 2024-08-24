package com.jordyma.blink.folder.repository

import com.jordyma.blink.folder.entity.Folder
import com.jordyma.blink.user.entity.User
import java.util.*

interface FolderRepositoryCustom {

    fun findAllByUser(user: User): List<Folder>

    fun findById(id: Long): Optional<Folder>
}