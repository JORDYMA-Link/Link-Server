package com.jordyma.blink.folder.repository.impl

import com.jordyma.blink.folder.entity.Folder
import com.jordyma.blink.folder.entity.QFolder
import com.jordyma.blink.user.entity.User
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class FolderRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) {
    fun findAllByUser(user: User): List<Folder> {
        return queryFactory.select(QFolder.folder)
            .from(QFolder.folder)
            .where(QFolder.folder.user.eq(user))
            .fetch()
    }

}