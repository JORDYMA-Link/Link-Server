package com.jordyma.blink.folder.repository.impl

import com.jordyma.blink.folder.entity.Folder
import com.jordyma.blink.folder.entity.QFolder
import com.jordyma.blink.folder.repository.FolderRepositoryCustom
import com.jordyma.blink.user.entity.User
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class FolderRepositoryCustomImpl (
    private val queryFactory: JPAQueryFactory
): FolderRepositoryCustom {

    override fun findAllByUser(user: User): List<Folder> {
        return queryFactory.select(QFolder.folder)
            .from(QFolder.folder)
            .where(
                QFolder.folder.user.eq(user)
                    .and(QFolder.folder.deletedAt.isNull),
            )
            .fetch()
    }

    override fun findById(id: Long): Optional<Folder> {
        return Optional.ofNullable(
            queryFactory.select(QFolder.folder)
                .from(QFolder.folder)
                .where(
                    QFolder.folder.id.eq(id),
                    QFolder.folder.deletedAt.isNull,
                )
                .fetchOne()
        )
    }

}