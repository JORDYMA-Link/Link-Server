package com.jordyma.blink.folder.repository

import com.jordyma.blink.folder.entity.Folder
import com.jordyma.blink.user.entity.User
import org.openqa.selenium.print.PageSize
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface FolderRepository : FolderRepositoryCustom, JpaRepository<Folder, Long> {
//    fun findByUser(user: User): List<Folder>

    override fun findAllByUser(user: User): List<Folder>

    @Query("select f from Folder f where f.user =:user and f.isUnclassified =true")
    fun findUnclassified(user: User): Folder?

    @Query("select f from Folder f where f.user =:user and f.name =:status")
    fun findFailed(user: User?, status: String): Folder?

    override fun findById(id: Long): Optional<Folder>
}