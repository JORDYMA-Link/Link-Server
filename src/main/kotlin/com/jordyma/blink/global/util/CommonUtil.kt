package com.jordyma.blink.global.util

import kotlin.math.pow

object CommonUtil {
    const val BEARER_PREFIX: String = "Bearer "


    fun parseTokenFromBearer(bearerToken: String?): String? {
        return bearerToken?.substring(BEARER_PREFIX.length)
    }
}