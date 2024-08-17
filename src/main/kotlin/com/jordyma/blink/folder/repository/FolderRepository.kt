package com.jordyma.blink.folder.repository

import com.jordyma.blink.folder.entity.Folder
import com.jordyma.blink.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface FolderRepository : JpaRepository<Folder, Long> {
    fun findByUser(user: User): List<Folder>

    fun findAllByUser(user: User): List<Folder>

    @Query("select f from Folder f where f.user =:user and f.isUnclassified =true")
    fun findUnclassified(user: User): Folder?

    @Query("select f from Folder f where f.user =:user and f.name =:요약실패")
    fun findFailed(user: User?): Folder?
}