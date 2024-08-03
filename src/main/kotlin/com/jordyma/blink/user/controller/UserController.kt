package com.jordyma.blink.user.controller

import com.jordyma.blink.user.entity.User
import com.jordyma.blink.user.service.UserService
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


//@Tag(name = "user", description = "유저 API")
@RestController
@RequestMapping("/user")
class UserController (
    private val userService: UserService,
){

}