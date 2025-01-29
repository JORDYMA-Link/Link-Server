package com.jordyma.blink.global.config

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory
import io.awspring.cloud.sqs.listener.acknowledgement.handler.AcknowledgementMode
import io.awspring.cloud.sqs.support.converter.SqsMessagingMessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsCredentials
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsAsyncClient

@Configuration
class AwsSQSConfig(
    @Value("\${spring.cloud.aws.credentials.access-key}")
    private val awsAccessKey: String,
    @Value("\${spring.cloud.aws.credentials.secret-key}")
    private val awsSecretKey: String,
    @Value("\${spring.cloud.aws.region.static}")
    private val region: String,
) {
    /**
     * AWS SQS 클라이언트
     */
    @Bean
    fun sqsAsyncClient(): SqsAsyncClient {
        return SqsAsyncClient.builder()
            .credentialsProvider {
                object : AwsCredentials {
                    override fun accessKeyId(): String {
                        return awsAccessKey
                    }

                    override fun secretAccessKey(): String {
                        return awsSecretKey
                    }
                }
            }
            .region(Region.of(region))
            .build()
    }

    /**
     * SysAsyncClient를 사용하여 실제 메세지를 수신하는 역할을 함
     */
    @Bean
    fun defaultSqsListenerContainerFactory(sqsAsyncClient: SqsAsyncClient): SqsMessageListenerContainerFactory<Any> {
        val messageConverter = SqsMessagingMessageConverter()
        messageConverter.setPayloadTypeMapper { null }

        return SqsMessageListenerContainerFactory
            .builder<Any>()
            // 수동으로 메세지를 삭제하기 위해 AcknowledgementMode.MANUAL로 설정
            .configure { opt ->
                opt.acknowledgementMode(AcknowledgementMode.MANUAL)
                opt.messageConverter(messageConverter)
            }
            .sqsAsyncClient(sqsAsyncClient)
            .build()
    }
}