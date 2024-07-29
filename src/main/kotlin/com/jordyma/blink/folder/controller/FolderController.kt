package com.jordyma.blink.folder.controller

import com.jordyma.blink.folder.dto.request.FolderCreateReqDto
import com.jordyma.blink.folder.dto.request.OnboardingReqDto
import com.jordyma.blink.folder.dto.response.FolderCreateResDto
import com.jordyma.blink.folder.dto.response.OnboardingResDto
import com.jordyma.blink.folder.entity.Folder
import com.jordyma.blink.folder.service.FolderService
import com.jordyma.blink.global.exception.ApplicationException
import com.jordyma.blink.global.exception.ErrorCode
import com.jordyma.blink.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "folder", description = "폴더 API")
@RestController
@RequestMapping("/folder")
class FolderController (
    private val folderService: FolderService,
    private val userService: UserService,
){

    // TODO: @RequestUserId 적용 > request에서 userId 삭제
    @PostMapping(value = ["/onboarding"])
    @Operation(summary = "온보딩 주제 선택 API", description =
            "'경제' > 'ECONOMY'\n" + "'기획' > 'PLANNING'\n" + "'개발' > 'DEVELOPMENT'\n" + "'독서' > 'READING'\n" + "'동물' > 'ANIMALS'\n" + "'디자인' > 'DESIGN'\n"
                    + "'아이돌' > 'IDOL'\n" + "'여행' > 'TRAVEL'\n" + "'영감' > 'INSPIRATION'\n" + "'옷' > 'CLOTHING'\n" + "'요리' > 'COOKING'\n" + "'의료' > 'MEDICAL'")
    fun createOnboarding(@RequestBody request: OnboardingReqDto): ResponseEntity<OnboardingResDto> {

        val user = userService.find(request.userId) // TODO: 수정

        val folders = mutableListOf<Folder>()
        for (topic in request.topics) {
            val folder = folderService.create(user, topic)
            folders.add(folder)
        }
        val ids = folders.mapNotNull { it.id }

        return ResponseEntity.ok(OnboardingResDto(ids))
    }

    // TODO: @RequestUserId 적용 > request에서 userId 삭제
    @PostMapping(value = [""])
    @Operation(summary = "폴더 생성 API")
    fun createFolder(@RequestBody request: FolderCreateReqDto): ResponseEntity<FolderCreateResDto> {

        val user = userService.find(request.userId) // TODO: 수정
        val folder = folderService.create(user, request.topic)
        folder.id ?: throw ApplicationException(ErrorCode.SHOULD_NOT_NULL, "폴더 생성 실패", Throwable())

        return ResponseEntity.ok(FolderCreateResDto(folder.id))
    }
}
