package com.jordyma.blink.fcm.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import java.io.ByteArrayInputStream
import java.io.IOException


@Configuration
class FcmConfig(
    @Value("\${firebase.account-key}") private val serviceAccountKey: String
) {

    @Bean
    @Throws(IOException::class)
    fun initializeFirebase(): FirebaseApp {
        val serviceAccount =
            ByteArrayInputStream(serviceAccountKey.toByteArray())

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()

        return FirebaseApp.initializeApp(options)
    }

    @Bean
    @DependsOn("initializeFirebase")
    fun firebaseMessaging(): FirebaseMessaging {
        return FirebaseMessaging.getInstance()
    }
}