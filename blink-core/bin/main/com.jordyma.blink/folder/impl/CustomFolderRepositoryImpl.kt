package com.jordyma.blink.folder.impl

import com.jordyma.blink.folder.Folder
import com.jordyma.blink.folder.QFolder
import com.jordyma.blink.folder.CustomFolderRepository
import com.jordyma.blink.user.User
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.transaction.Transactional
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
@Transactional
class CustomFolderRepositoryImpl(
    private val queryFactory: JPAQueryFactory
): CustomFolderRepository {
    override fun findAllByUser(user: User): List<Folder> {
        return queryFactory.select(QFolder.folder)
            .from(QFolder.folder)
            .where(QFolder.folder.user.eq(user), QFolder.folder.deletedAt.isNull)
            .fetch()
    }

    override fun deleteFolder(folder: Folder) {
        queryFactory.update(QFolder.folder)
            .where(QFolder.folder.eq(folder))
            .set(QFolder.folder.deletedAt, LocalDateTime.now())
            .execute()
    }

}