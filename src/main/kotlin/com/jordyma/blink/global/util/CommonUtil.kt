package com.jordyma.blink.global.util

import kotlin.math.pow

object CommonUtil {
    const val BEARER_PREFIX: String = "Bearer "


    fun parseTokenFromBearer(bearerToken: String?): String? {
        return bearerToken?.substring(BEARER_PREFIX.length)
    }

    fun round(value: Double?, precision: Int): Double? {
        if (value == null) {
            return null
        }
        val scale = 10.0.pow(precision.toDouble())
        return Math.round(value * scale).toDouble() / scale
    }
}