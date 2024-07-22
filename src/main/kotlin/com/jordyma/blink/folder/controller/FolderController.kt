package com.jordyma.blink.folder.controller

import com.jordyma.blink.auth.jwt.user_account.UserAccount
import com.jordyma.blink.feed.dto.FeedCalendarResponseDto
import com.jordyma.blink.folder.dto.CreateFolderRequestDto
import com.jordyma.blink.folder.dto.FolderDto
import com.jordyma.blink.folder.dto.GetFolderListResponseDto
import com.jordyma.blink.folder.dto.UpdateFolderRequestDto
import com.jordyma.blink.folder.service.FolderService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/folders")
class FolderController(private val folderService: FolderService) {

    @Operation(summary = "보관함 폴더 리스트 조회", description = "")
    @GetMapping("")
    fun getFolders(
        @AuthenticationPrincipal userAccount: UserAccount,
    ): ResponseEntity<GetFolderListResponseDto> {
        val response = folderService.getFolders(user = userAccount)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "폴더 삭제", description = "")
    @DeleteMapping("{folderId}")
    fun deleteFolder(
        @PathVariable("folderId") folderId: Long,
        @AuthenticationPrincipal userAccount: UserAccount,
    ): ResponseEntity<Unit> {
        folderService.delete(userAccount = userAccount, folderId = folderId)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "폴더별 피드 리스트 조회", description = "")
    @GetMapping("{folderId}/feeds")
    fun getFeedsByFolder(
        @PathVariable("folderId") folderId: Long,
        @AuthenticationPrincipal userAccount: UserAccount,
    ): ResponseEntity<FeedCalendarResponseDto> {
        val response = folderService.getFeedsByFolder(userAccount = userAccount, folderId = folderId)
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