package com.jordyma.blink

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BlinkApplication

fun main(args: Array<String>) {

    runApplication<BlinkApplication>(*args)

}
