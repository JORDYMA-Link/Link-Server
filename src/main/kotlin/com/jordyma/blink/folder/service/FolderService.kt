package com.jordyma.blink.folder.service

import com.jordyma.blink.auth.jwt.user_account.UserAccount
import com.jordyma.blink.feed.dto.FeedDto
import com.jordyma.blink.feed.entity.Source
import com.jordyma.blink.feed.repository.FeedRepository
import com.jordyma.blink.folder.dto.request.CreateFolderRequestDto
import com.jordyma.blink.folder.dto.request.GetFeedsByFolderRequestDto
import com.jordyma.blink.folder.dto.request.UpdateFolderRequestDto
import com.jordyma.blink.folder.dto.response.FolderDto
import com.jordyma.blink.folder.dto.response.GetFeedsByFolderResponseDto
import com.jordyma.blink.folder.dto.response.GetFolderListResponseDto
import com.jordyma.blink.folder.entity.Folder
import com.jordyma.blink.folder.repository.FolderRepository
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
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

    @Transactional
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

    fun getFeedsByFolder(userAccount: UserAccount, folderId: Long, getFeedsByFolderRequestDto: GetFeedsByFolderRequestDto): GetFeedsByFolderResponseDto {
        val userId = userAccount.userId;
        val cursor = getFeedsByFolderRequestDto.cursor;
        val pageSize = getFeedsByFolderRequestDto.pageSize;

        val user = userRepository.findById(userId).orElseThrow {
            ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.")
        }
        val folder = folderRepository.findById(folderId).orElseThrow {
            ApplicationException(ErrorCode.NOT_FOUND, "폴더를 찾을 수 없습니다.")
        }

        if (folder.user != user) {
            throw ApplicationException(ErrorCode.UNAUTHORIZED, "폴더 조회 권한이 없습니다.")
        }


        val feeds = feedRepository.findAllByFolder(folder, cursor, pageSize!!);
        val feedList = feeds.map { feed ->
            FeedDto(
                folderId = feed.folder!!.id,
                folderName = feed.folder!!.name,
                feedId = feed.id!!,
                title = feed.title,
                summary = feed.summary,
                platform = feed.platform ?: "",
                platformUrl = Source.getBrunchByName(feed.platform ?: "")!!.image,
                isMarked = feed.isMarked,
                keywords = feed.keywords.map { it.content },
            )
        }

        return GetFeedsByFolderResponseDto(folderId=folder.id!!, folderName=folder.name, feedList=feedList)
    }

    @Transactional
    fun create(userAccount: UserAccount, requestDto: CreateFolderRequestDto): FolderDto {
        val userId = userAccount.userId;
        val user = userRepository.findById(userAccount.userId).orElseThrow {
            ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.")
        }

        val folder = Folder(
            name = requestDto.name,
            user = user,
            count = 0,
            isUnclassified = requestDto.name == "미분류"
        )

        val savedFolder = folderRepository.save(folder)

        return FolderDto(
            id = savedFolder.id,
            name = savedFolder.name,
            feedCount = savedFolder.count
        )
    }

    @Transactional
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

    // 유저의 미분류 폴더 찾기
    fun getUnclassified(userAccount: UserAccount): Folder?{
        val user = userRepository.findById(userAccount.userId).orElseThrow {
            ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.")
        }

        // 미분류 폴더 찾기
        var folder = folderRepository.findUnclassified(user)
        if(folder == null){     // 없으면 생성
            val request = CreateFolderRequestDto(
                name = "미분류"
            )
            folder = getFolderById(create(userAccount, request).id!!)
        }

        return folder
    }

    // 유저의 요약 실패 폴더 찾기
    fun getFailed(userAccount: UserAccount): Folder?{
        val user = userRepository.findById(userAccount.userId).orElseThrow {
            ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.")
        }

        // 요약 실패 폴더 찾기
        var folder = folderRepository.findFailed(user, "FAILED")
        if(folder == null){     // 없으면 생성
            val request = CreateFolderRequestDto(
                name = "요약실패"
            )
            folder = getFolderById(create(userAccount, request).id!!)
        }

        return folder
    }

    fun getFolderById(folderId: Long): Folder{
        return folderRepository.findById(folderId).orElseThrow {
            ApplicationException(ErrorCode.FOLDER_NOT_FOUND, "폴더를 찾을 수 없습니다.")
        }

    }
}