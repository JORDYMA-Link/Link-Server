package com.jordyma.blink.fcm.client

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Component

@Component
class FcmClient(private val firebaseMessaging: FirebaseMessaging
) {

    fun send(message: Message) {
        val firebaseMessaging: FirebaseMessaging = FirebaseMessaging.getInstance()
        firebaseMessaging.send(message)
    }

    fun createMessage(
        token: String,
        title: String,
        body: String,
        data: Map<String, String> = emptyMap()
    ): Message {
        return Message.builder()
            .setToken(token)
            .setNotification(
                Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build()
            )
            .putAllData(data)
            .build()
    }
}