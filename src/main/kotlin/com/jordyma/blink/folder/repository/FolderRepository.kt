package com.jordyma.blink.folder.repository

import com.jordyma.blink.folder.entity.Folder
import org.springframework.data.jpa.repository.JpaRepository

interface FolderRepository : JpaRepository<Folder, Int> {
}