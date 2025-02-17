package com.jordyma.blink.scheduler

import com.jordyma.blink.jobs.user.UserDataNotificationJobConfig
import com.jordyma.blink.logger
import org.springframework.batch.core.JobParameter
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled

@Configuration
class UserDataNotificationJobScheduler(
    private val jobLauncher: JobLauncher,
    private val userDataNotificationJobConfig: UserDataNotificationJobConfig,
) {
    @Scheduled(cron = "\${scheduler.user-notification.cron}")
    fun doUserDataNotificationScheduler() {
        val jobParameters = JobParameters(
            mapOf("time" to JobParameter(System.currentTimeMillis(), Long::class.java))
        )

        runCatching {
            jobLauncher.run(userDataNotificationJobConfig.userDataNotificationJob(), jobParameters)
        }.onFailure { error ->
            logger().error(
                userDataNotificationJobConfig.userDataNotificationJob().name,
                error.message
            )
        }
    }
}