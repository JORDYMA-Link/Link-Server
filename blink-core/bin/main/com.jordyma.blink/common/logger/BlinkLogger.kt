package com.jordyma.blink.common.logger

import org.slf4j.LoggerFactory

interface BlinkLogger {
    fun info(message: String)
    fun error(message: String)
    fun error(message: String, throwable: Throwable)
}

class Slf4jBlinkLogger : BlinkLogger {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun info(message: String) = log.info(message)
    override fun error(message: String) = log.error(message)
    override fun error(message: String, throwable: Throwable) = log.error(message, throwable)
}