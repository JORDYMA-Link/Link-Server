package com.jordyma.blink.folder.controller

import com.jordyma.blink.folder.service.FolderService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import com.jordyma.blink.auth.jwt.user_account.UserAccount
import com.jordyma.blink.folder.dto.*
import com.jordyma.blink.folder.dto.request.*
import com.jordyma.blink.folder.dto.response.*
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "folder", description = "폴더 API")
@RequestMapping("/api/folders")
class FolderController(
    private val folderService: FolderService
) {

    @PostMapping(value = ["/onboarding"])
    @Operation(summary = "온보딩 주제 선택 API")
    fun createOnboarding(@AuthenticationPrincipal userAccount: UserAccount,
                         @RequestBody request: OnboardingReqDto
    ): ResponseEntity<OnboardingResDto> {

        val folders: MutableList<FolderDto> = mutableListOf()
        for (topic in request.topics) {
            val folder = folderService.create(userAccount, CreateFolderRequestDto(topic))
            folders.add(folder)
        }
        val ids = folders.mapNotNull { it.id }

        return ResponseEntity.ok(OnboardingResDto(ids))
    }

    @Operation(summary = "보관함 폴더 리스트 조회", description = "")
    @GetMapping("")
    fun getFolders(
        @AuthenticationPrincipal userAccount: UserAccount,
    ): ResponseEntity<GetFolderListResponseDto> {
        val response = folderService.getFolders(userAccount = userAccount)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "폴더 삭제", description = "")
    @DeleteMapping("{folderId}")
    fun deleteFolder(
        @PathVariable("folderId") folderId: Long,
        @AuthenticationPrincipal userAccount: UserAccount,
    ): ResponseEntity<Unit> {
        folderService.delete(userAccount = userAccount, folderId = folderId)
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "폴더별 피드 리스트 조회", description = "")
    @GetMapping("{folderId}/feeds")
    fun getFeedsByFolder(
        @PathVariable("folderId") folderId: Long,
        @AuthenticationPrincipal userAccount: UserAccount,
        @ModelAttribute getFeedsByFolderRequestDto: GetFeedsByFolderRequestDto
    ): ResponseEntity<GetFeedsByFolderResponseDto> {
        val response = folderService.getFeedsByFolder(
            userAccount = userAccount,
            folderId = folderId,
            getFeedsByFolderRequestDto = getFeedsByFolderRequestDto,
        )

        return ResponseEntity.ok(response)
    }

    @Operation(summary = "폴더 생성", description = "")
    @PostMapping("")
    fun createFolder(
        @AuthenticationPrincipal userAccount: UserAccount,
        @RequestBody requestDto: CreateFolderRequestDto,
    ): ResponseEntity<FolderDto> {
        val response = folderService.create(userAccount = userAccount, requestDto = requestDto)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "피드에 폴더 지정", description = "선택한 폴더가 존재하면 지정, 존재하지 않으면 생성")
    @PatchMapping("/feed")
    fun createFeedFolder(
        @AuthenticationPrincipal userAccount: UserAccount,
        @RequestBody requestDto: CreateFeedFolderRequestDto,
    ): ResponseEntity<FolderDto> {
        val response = folderService.createFeedFolder(userAccount = userAccount, requestDto = requestDto)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "폴더 수정", description = "")
    @PatchMapping("{folderId}")
    fun updateFolder(
        @AuthenticationPrincipal userAccount: UserAccount,
        @PathVariable("folderId") folderId: Long,
        @RequestBody requestDto: UpdateFolderRequestDto,
    ): ResponseEntity<FolderDto> {
        val response = folderService.update(userAccount = userAccount, folderId = folderId, requestDto = requestDto)
        return ResponseEntity.ok(response)
    }
}