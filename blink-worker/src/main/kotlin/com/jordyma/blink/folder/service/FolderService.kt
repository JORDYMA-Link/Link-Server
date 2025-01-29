package com.jordyma.blink.folder.service

import com.jordyma.blink.folder.dto.request.CreateFolderRequestDto
import com.jordyma.blink.folder.dto.response.FolderDto
import com.jordyma.blink.folder.dto.response.GetFolderListResponseDto
import com.jordyma.blink.folder.Folder
import com.jordyma.blink.folder.FolderRepository
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FolderService(
    private val folderRepository: FolderRepository,
    private val userRepository: UserRepository,
) {

    fun getFolders(userId: Long): GetFolderListResponseDto {
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
            return GetFolderListResponseDto(it)
        }

    }

    // 유저의 요약 실패 폴더 찾기
    fun getFailed(userId: Long): Folder?{
        val user = userRepository.findById(userId).orElseThrow {
            ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.")
        }

        // 요약 실패 폴더 찾기
        var folder = folderRepository.findFailed(user, "FAILED")
        if(folder == null){     // 없으면 생성
            val request = CreateFolderRequestDto(
                name = "요약실패"
            )
            folder = getFolderById(create(userId, request).id!!)
        }

        return folder
    }

    fun getFolderById(folderId: Long): Folder{
        return folderRepository.findById(folderId).orElseThrow {
            ApplicationException(ErrorCode.FOLDER_NOT_FOUND, "폴더를 찾을 수 없습니다.")
        }

    }

    @Transactional
    fun create(userId: Long, requestDto: CreateFolderRequestDto): FolderDto {
        val user = userRepository.findById(userId).orElseThrow {
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

    // 유저의 미분류 폴더 찾기
    fun getUnclassified(userId: Long): Folder {
        val user = userRepository.findById(userId).orElseThrow {
            ApplicationException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.")
        }

        // 미분류 폴더 찾기
        var folder = folderRepository.findUnclassified(user)
        if(folder == null){     // 없으면 생성
            val request = CreateFolderRequestDto(
                name = "미분류"
            )
            folder = getFolderById(create(userId, request).id!!)
        }

        return folder
    }
}