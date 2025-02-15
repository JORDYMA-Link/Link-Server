package com.jordyma.blink.folder.service

import com.jordyma.blink.feed.domain.FeedRepository
import com.jordyma.blink.feed.domain.Source
import com.jordyma.blink.feed.dto.FeedDto
import com.jordyma.blink.folder.Folder
import com.jordyma.blink.folder.FolderRepository
import com.jordyma.blink.folder.domain.model.FolderDto
import com.jordyma.blink.folder.domain.model.GetFeedsByFolderRequestDto
import com.jordyma.blink.folder.domain.model.GetFeedsByFolderResponseDto
import com.jordyma.blink.folder.domain.service.FolderService
import com.jordyma.blink.folder.dto.request.CreateFolderRequestDto
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.user.UserRepository
import org.springframework.stereotype.Service

@Service
class FolderServiceImpl(
    private val folderRepository: FolderRepository,
    private val feedRepository: FeedRepository,
    private val userRepository: UserRepository,
) : FolderService {

    override fun getFolders(userId: Long): List<FolderDto> {
        val user = userRepository.findById(userId).orElseThrow {
            ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.")
        }
        val folders = folderRepository.findAllByUser(user)

        folders.filter { folder ->
            !folder.isUnclassified
        }.map { folder ->
            FolderDto(
                id = folder.id,
                name = folder.name,
                feedCount = folder.count
            )
        }.let {
            return it
        }
    }

    override fun delete(userId: Long, folderId: Long) {
        val user = userRepository.findById(userId).orElseThrow {
            ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.")
        }
        val folder = folderRepository.findById(folderId).orElseThrow {
            ApplicationException(ErrorCode.NOT_FOUND, "폴더를 찾을 수 없습니다.")
        }

        if (folder.user != user) {
            throw ApplicationException(ErrorCode.UNAUTHORIZED, "폴더 삭제 권한이 없습니다.")
        }

        // TODO: repository 옮기기
        feedRepository.deleteKeywords(folder)
        feedRepository.deleteRecommend(folder)
        feedRepository.deleteAllByFolder(folder)

        folderRepository.deleteFolder(folder)
    }

    override fun signOutDelete(userId: Long) {
        val user = userRepository.findById(userId).orElseThrow {
            ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.")
        }

        val folders = folderRepository.findFoldersByUser(user)
        folders.forEach { folder ->
            feedRepository.deleteKeywords(folder)
            feedRepository.deleteRecommend(folder)
            feedRepository.deleteAllByFolder(folder)

            folderRepository.deleteFolder(folder)
        }
    }

    override fun getFeedsByFolder(
        userId: Long,
        folderId: Long,
        request: GetFeedsByFolderRequestDto
    ): GetFeedsByFolderResponseDto {
        val cursor = request.cursor;
        val pageSize = request.pageSize;

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
                platformImage = Source.getBrunchByName(feed.platform ?: "")!!.image,
                isMarked = feed.isMarked,
                keywords = feed.keywords.map { it.content },
                sourceUrl = feed.originUrl
            )
        }

        return GetFeedsByFolderResponseDto(
            folderId = folder.id!!,
            folderName = folder.name,
            feedList = feedList
        )
    }

    override fun create(userId: Long, folderName: String): FolderDto {
        val user = userRepository.findById(userId).orElseThrow {
            ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.")
        }

        folderRepository.findAllByUser(user).forEach {
            if (it.name == folderName) throw ApplicationException(ErrorCode.FOLDER_ALREADY_EXISTS, "이미 존재하는 폴더명입니다.")
        }

        val folder = Folder(
            name = folderName,
            user = user,
            count = 0,
            isUnclassified = folderName == "미분류"
        )

        val savedFolder = folderRepository.save(folder)

        return FolderDto(
            id = savedFolder.id,
            name = savedFolder.name,
            feedCount = savedFolder.count
        )
    }

    override fun update(userId: Long, folderId: Long, folderName: String): FolderDto {
        val user = userRepository.findById(userId).orElseThrow {
            ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.")
        }
        val folder = folderRepository.findById(folderId).orElseThrow {
            ApplicationException(ErrorCode.NOT_FOUND, "폴더를 찾을 수 없습니다.")
        }

        if (folder.user != user) {
            throw ApplicationException(ErrorCode.UNAUTHORIZED, "폴더 수정 권한이 없습니다.")
        }

        folder.name = folderName
        val savedFolder = folderRepository.save(folder)

        return FolderDto(
            id = savedFolder.id,
            name = savedFolder.name,
            feedCount = savedFolder.count
        )
    }

    override fun createFeedFolder(userId: Long, feedId: Long, folderName: String): FolderDto? {
        val user = userRepository.findById(userId)
            .orElseThrow { ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.") }

        val feed = feedRepository.findById(feedId)
            .orElseThrow { ApplicationException(ErrorCode.NOT_FOUND, "일치하는 feedId가 없습니다 : ${feedId}") }

        // 기존 폴더 cnt--
        if(feed.folder != null){
            feed.folder!!.decreaseCount()
            folderRepository.save(feed.folder!!)
        }

        val folder = folderRepository.findByName(folderName, user)
            ?: Folder(
                name = folderName,
                user = user,
                count = 1,
                isUnclassified = false
            )

        folder.increaseCount()
        folderRepository.save(folder)

        feed.updateFolder(folder)
        feedRepository.save(feed)

        return FolderDto(
            id = folder.id,
            name = folder.name,
            feedCount = folder.count,
        )
    }

    override fun getUnclassified(userId: Long): Folder {
        val user = userRepository.findById(userId).orElseThrow {
            ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.")
        }

        // 미분류 폴더 찾기
        var folder = folderRepository.findUnclassified(user)
        if(folder == null){     // 없으면 생성
            val request = CreateFolderRequestDto(
                name = "미분류"
            )
            folder = getFolderById(create(userId, request.name).id!!)
        }

        return folder
    }

    override fun getFailed(userId: Long): Folder? {
        val user = userRepository.findById(userId).orElseThrow {
            ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.")
        }

        // 요약 실패 폴더 찾기
        var folder = folderRepository.findFailed(user, "FAILED")
        if(folder == null){     // 없으면 생성
            val request = CreateFolderRequestDto(
                name = "요약실패"
            )
            folder = getFolderById(create(userId, request.name).id!!)
        }

        return folder
    }

    override fun getFolderById(folderId: Long): Folder {
        return folderRepository.findById(folderId).orElseThrow {
            ApplicationException(ErrorCode.FOLDER_NOT_FOUND, "폴더를 찾을 수 없습니다.")
        }
    }
}