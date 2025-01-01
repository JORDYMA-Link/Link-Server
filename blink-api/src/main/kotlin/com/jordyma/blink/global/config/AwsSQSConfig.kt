package com.jordyma.blink.global.config

import io.awspring.cloud.sqs.operations.SqsTemplate
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

    // 메세지 발송을 위한 SQS 템플릿 설정
    @Bean
    fun sqsTemplate(): SqsTemplate {
        return SqsTemplate.newTemplate(sqsAsyncClient())
    }
}