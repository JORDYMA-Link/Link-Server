package com.jordyma.blink

import org.slf4j.LoggerFactory
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication(exclude = [RedisAutoConfiguration::class])
@EnableBatchProcessing
@EnableScheduling
class BlinkBatchApplication
inline fun <reified T> T.logger() = LoggerFactory.getLogger(T::class.java)!!

fun main(args: Array<String>) {

    runApplication<BlinkBatchApplication>(*args)
}