package com.jordyma.blink.folder.controller

import com.jordyma.blink.folder.domain.service.FolderService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

import com.jordyma.blink.auth.jwt.user_account.UserAccount
import com.jordyma.blink.folder.domain.model.FolderDto
import com.jordyma.blink.folder.domain.model.GetFeedsByFolderRequestDto
import com.jordyma.blink.folder.domain.model.GetFeedsByFolderResponseDto
import com.jordyma.blink.folder.dto.request.CreateFeedFolderRequestDto
import com.jordyma.blink.folder.dto.request.CreateFolderRequestDto
import com.jordyma.blink.folder.dto.request.OnboardingReqDto
import com.jordyma.blink.folder.dto.request.UpdateFolderRequestDto
import com.jordyma.blink.folder.dto.response.GetFolderListResponseDto
import com.jordyma.blink.folder.dto.response.OnboardingResDto
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
            val folder = folderService.create(userAccount.userId, topic)
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
        val response = folderService.getFolders(userAccount.userId)
        return ResponseEntity.ok(GetFolderListResponseDto(response))
    }

    @Operation(summary = "폴더 삭제", description = "")
    @DeleteMapping("{folderId}")
    fun deleteFolder(
        @PathVariable("folderId") folderId: Long,
        @AuthenticationPrincipal userAccount: UserAccount,
    ): ResponseEntity<Unit> {
        folderService.delete(userAccount.userId, folderId)
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
            userAccount.userId,
            folderId,
            getFeedsByFolderRequestDto,
        )

        return ResponseEntity.ok(response)
    }

    @Operation(summary = "폴더 생성", description = "")
    @PostMapping("")
    fun createFolder(
        @AuthenticationPrincipal userAccount: UserAccount,
        @RequestBody requestDto: CreateFolderRequestDto,
    ): ResponseEntity<FolderDto> {
        val response = folderService.create(userAccount.userId, requestDto.name)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "피드에 폴더 지정", description = "선택한 폴더가 존재하면 지정, 존재하지 않으면 생성")
    @PatchMapping("/feed")
    fun createFeedFolder(
        @AuthenticationPrincipal userAccount: UserAccount,
        @RequestBody requestDto: CreateFeedFolderRequestDto,
    ): ResponseEntity<FolderDto> {
        val response = folderService.createFeedFolder(userAccount.userId, requestDto.feedId, requestDto.name)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "폴더 수정", description = "")
    @PatchMapping("{folderId}")
    fun updateFolder(
        @AuthenticationPrincipal userAccount: UserAccount,
        @PathVariable("folderId") folderId: Long,
        @RequestBody requestDto: UpdateFolderRequestDto,
    ): ResponseEntity<FolderDto> {
        val response = folderService.update(userAccount.userId, folderId, requestDto.name)
        return ResponseEntity.ok(response)
    }
}