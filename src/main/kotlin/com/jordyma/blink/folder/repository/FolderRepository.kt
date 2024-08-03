package com.jordyma.blink.folder.repository

import com.jordyma.blink.folder.entity.Folder
import com.jordyma.blink.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface FolderRepository : JpaRepository<Folder, Long> {
    fun findByUser(user: User): List<Folder>

    fun findAllByUser(user: User): List<Folder>
}