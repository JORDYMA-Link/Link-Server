package com.jordyma.blink.folder.repository.impl

import com.jordyma.blink.folder.entity.Folder
import com.jordyma.blink.folder.entity.QFolder
import com.jordyma.blink.folder.repository.CustomFolderRepository
import com.jordyma.blink.user.entity.User
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class CustomFolderRepositoryImpl(
    private val queryFactory: JPAQueryFactory
): CustomFolderRepository {
    override fun findAllByUser(user: User): List<Folder> {
        return queryFactory.select(QFolder.folder)
            .from(QFolder.folder)
            .where(QFolder.folder.user.eq(user), QFolder.folder.deletedAt.isNull)
            .fetch()
    }

}