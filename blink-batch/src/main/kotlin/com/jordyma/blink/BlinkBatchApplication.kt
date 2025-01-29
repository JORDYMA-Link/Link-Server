package com.jordyma.blink

import org.slf4j.LoggerFactory
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import kotlin.system.exitProcess

@SpringBootApplication(exclude = [RedisAutoConfiguration::class])
@EnableBatchProcessing
@EnableScheduling
class BlinkBatchApplication
inline fun <reified T> T.logger() = LoggerFactory.getLogger(T::class.java)!!

fun main(args: Array<String>) {
    val name: String? = System.getenv("job.name")
    if (name != null) {
        System.setProperty("spring.batch.job.name", name)
    }

    val exit = SpringApplication.exit(
        SpringApplicationBuilder(BlinkBatchApplication::class.java).run(*args),
    )
    exitProcess(exit)
}