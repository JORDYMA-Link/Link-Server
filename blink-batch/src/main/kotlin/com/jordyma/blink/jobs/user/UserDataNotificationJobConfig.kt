package com.jordyma.blink.jobs.user

import com.jordyma.blink.domain.dto.UserDataNotificationDto
import com.jordyma.blink.domain.service.SendUserDataNotificationService
import com.jordyma.blink.logger
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JdbcCursorItemReader
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource


@Configuration
class UserDataNotificationJobConfig(
        private val jobRepository: JobRepository,
        @Qualifier("batchTransactionManager")
        private val batchTransactionManager: PlatformTransactionManager,
        private val dataSource: DataSource,
        private val sendMessageService: SendUserDataNotificationService,
) {
    companion object {
        const val JOB_NAME = "UserDataNotificationJobConfig"
        const val STEP_NAME = "UserDataNotificationJobStep"
    }

    @Bean
    fun userDataNotificationJob(): Job {
        return JobBuilder(JOB_NAME, jobRepository)
            .start(userDataNotificationJobStep())
            .build()
    }

    @Bean
    fun userDataNotificationJobStep(): Step {
        return StepBuilder(STEP_NAME, jobRepository)
            .chunk<UserDataNotificationDto, UserDataNotificationDto>(1, batchTransactionManager)
            .reader(userDataNotificationReader())
            .writer(userDataNotificationWriter())
            .allowStartIfComplete(true)
            .build()
    }

    @Bean
    @StepScope
    fun userDataNotificationReader(): JdbcCursorItemReader<UserDataNotificationDto> {
        val sql = """
        WITH new_users AS (
            SELECT id
            FROM user 
            WHERE created_at BETWEEN CURRENT_DATE - INTERVAL 1 DAY AND CURRENT_DATE
        )
        SELECT 
            (SELECT COUNT(*) FROM new_users) AS new_users_count,
            (SELECT COUNT(*) FROM user WHERE deleted_at IS NULL AND created_at < CURRENT_DATE) AS total_users,
            (
                SELECT COUNT(*)
                FROM feed f
                JOIN folder fol ON f.folder_id = fol.id
                WHERE fol.user_id IN (SELECT id FROM new_users)
                AND f.created_at BETWEEN CURRENT_DATE - INTERVAL 1 DAY AND CURRENT_DATE
            ) AS new_users_feed_count,
            (
                SELECT COUNT(*)
                FROM feed f
                JOIN folder fol ON f.folder_id = fol.id
                WHERE f.created_at >= CURRENT_DATE - INTERVAL 1 DAY
                AND f.created_at < CURRENT_DATE
                AND fol.user_id NOT IN (SELECT id FROM new_users)
                AND fol.user_id NOT IN (55, 56, 3, 4, 52, 57, 53, 20, 172, 262)
            ) AS existing_users_feed_count
        """

        val reader = JdbcCursorItemReader<UserDataNotificationDto>()
        reader.setSql(sql.trimIndent())
        reader.setDataSource(dataSource)
        reader.setFetchSize(1)
        reader.setRowMapper { rs, _ ->
            UserDataNotificationDto(
                newUserCount = rs.getInt("new_users_count"),
                totalUserCount = rs.getInt("total_users"),
                newUserFeed = rs.getInt("new_users_feed_count"),
                existingUserFeed = rs.getInt("existing_users_feed_count")
            )
        }
        reader.setName("userDataNotificationReader")
        reader.afterPropertiesSet()

        return reader
    }

    @Bean
    fun userDataNotificationWriter(): ItemWriter<UserDataNotificationDto> {
        return ItemWriter { items ->
            items.firstOrNull()?.let { dto ->
                sendMessageService.sendUserDataNotification(dto)
            }
        }
    }
}
