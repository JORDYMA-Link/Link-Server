package com.jordyma.blink.folder.service

import com.jordyma.blink.auth.jwt.user_account.UserAccount
import com.jordyma.blink.feed.dto.FeedCalendarResponseDto
import com.jordyma.blink.feed.dto.FeedDto
import com.jordyma.blink.feed.repository.FeedRepository
import com.jordyma.blink.folder.dto.*
import com.jordyma.blink.folder.entity.Folder
import com.jordyma.blink.folder.repository.FolderRepository
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.user.repository.UserRepository
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FolderService(
    private val folderRepository: FolderRepository,
    private val feedRepository: FeedRepository,
    private val userRepository: UserRepository,
) {
    fun getFolders(userAccount: UserAccount): GetFolderListResponseDto {
        val userId = userAccount.userId;
        val user = userRepository.findById(userId).orElseThrow {
            ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.")
        }
        val folders = folderRepository.findAllByUser(user)

        folders.map { folder ->
            FolderDto(
                id = folder.id,
                name = folder.name,
                feedCount = folder.count
            )
        }.let {
            return GetFolderListResponseDto(it)
        }

    }

    fun delete(userAccount: UserAccount, folderId: Long): Unit {
        val userId = userAccount.userId;
        val user = userRepository.findById(userId).orElseThrow {
            ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.")
        }
        val folder = folderRepository.findById(folderId).orElseThrow {
            ApplicationException(ErrorCode.NOT_FOUND, "폴더를 찾을 수 없습니다.")
        }

        if (folder.user != user) {
            throw ApplicationException(ErrorCode.UNAUTHORIZED, "폴더 삭제 권한이 없습니다.")
        }

        feedRepository.deleteAllByFolder(folder)

        folderRepository.delete(folder)
    }

    fun getFeedsByFolder(userAccount: UserAccount, folderId: Long): GetFeedsByFolderRequestDto {
        val userId = userAccount.userId;
        val user = userRepository.findById(userId).orElseThrow {
            ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.")
        }
        val folder = folderRepository.findById(folderId).orElseThrow {
            ApplicationException(ErrorCode.NOT_FOUND, "폴더를 찾을 수 없습니다.")
        }

        if (folder.user != user) {
            throw ApplicationException(ErrorCode.UNAUTHORIZED, "폴더 조회 권한이 없습니다.")
        }

        val feeds = feedRepository.findAllByFolder(folder)
        val feedList = feeds.map { feed ->
            FeedDto(
                folderId = feed.folder.id,
                folderName = feed.folder.name,
                feedId = feed.id!!,
                title = feed.title,
                summary = feed.summary,
                platform = feed.platform,
                sourceUrl = feed.originUrl,
                isMarked = feed.isMarked,
                keywords = feed.keywords.map { it.keyword },
            )
        }

        return GetFeedsByFolderRequestDto(feedList)
    }

    fun create(userAccount: UserAccount, requestDto: CreateFolderRequestDto): FolderDto {
        val userId = userAccount.userId;
        val user = userRepository.findById(userAccount.userId).orElseThrow {
            ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.")
        }
        val folder = Folder(
            name = requestDto.name,
            user = user,
            count = 0,
        )

        val savedFolder = folderRepository.save(folder)

        return FolderDto(
            id = savedFolder.id,
            name = savedFolder.name,
            feedCount = savedFolder.count
        )
    }

    fun update(userAccount: UserAccount, folderId: Long, requestDto: UpdateFolderRequestDto): FolderDto {
        val user = userRepository.findById(userAccount.userId).orElseThrow {
            ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.")
        }
        val folder = folderRepository.findById(folderId).orElseThrow {
            ApplicationException(ErrorCode.NOT_FOUND, "폴더를 찾을 수 없습니다.")
        }

        if (folder.user != user) {
            throw ApplicationException(ErrorCode.UNAUTHORIZED, "폴더 수정 권한이 없습니다.")
        }

        folder.name = requestDto.name
        val savedFolder = folderRepository.save(folder)

        return FolderDto(
            id = savedFolder.id,
            name = savedFolder.name,
            feedCount = savedFolder.count
        )
    }
}