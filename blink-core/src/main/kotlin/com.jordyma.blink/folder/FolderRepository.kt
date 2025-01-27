package com.jordyma.blink.folder.repository

import com.jordyma.blink.folder.entity.Folder
import com.jordyma.blink.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface FolderRepository : JpaRepository<Folder, Long>, CustomFolderRepository {
//    fun findByUser(user: User): List<Folder>

    override fun findAllByUser(user: User): List<Folder>

    @Query("select f from Folder f where f.user =:user and f.isUnclassified =true")
    fun findUnclassified(user: User): Folder?

    @Query("select f from Folder f where f.user =:user and f.name =:status")
    fun findFailed(user: User?, status: String): Folder?

    @Query("select f from Folder f where f.user =:user and f.name =:name")
    fun findByName(name: String, user: User): Folder?

    @Query("select f from Folder f where f.user =:user")
    fun findFoldersByUser(user: User): List<Folder>

    // override fun delete(folder: Folder): Unit
}