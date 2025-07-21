package com.jordyma.blink.auth.dto.response

data class GoogleTokenUserResponseDto (
        val iss: String?,                // 발급자
        val azp: String?,                // 승인된 파티 (Authorized party)
        val aud: String?,                // Audience (클라이언트 ID)
        val sub: String,                // 사용자 고유 ID
        val at_hash: String?,           // access token hash
        val hd: String?,                // hosted domain
        val email: String,
        val email_verified: Boolean,
        val iat: Long?,                 // issued at (timestamp)
        val exp: Long?,                 // expiration time (timestamp)
        val nonce: String?,             // nonce (요청 시 사용한 값)
        val name: String? = null,       // 선택: userinfo API에 포함될 수도 있음
        val picture: String? = null     // 선택: userinfo API에 포함될 수도 있음
)