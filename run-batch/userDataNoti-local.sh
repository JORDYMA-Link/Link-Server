#!/bin/bash

# 로컬 테스트용 로그 디렉토리 설정
pwd
LOG_DIR="run-batch/logs/batch"
LOG_FILE="$LOG_DIR/user_data_noti_job_$(date '+%Y-%m-%d').log"

# 로그 디렉토리 생성
mkdir -p $LOG_DIR

echo "[$(date '+%Y-%m-%d %H:%M:%S')] Starting UserDataNotification Job (Local Test)" >> "$LOG_FILE"

# 권한 확인
pwd
cd ..
pwd
ls -l blink-batch/build/libs/blink-batch-0.0.1-SNAPSHOT.jar
chmod +x blink-batch/build/libs/blink-batch-0.0.1-SNAPSHOT.jar

# JAR 파일 경로를 상위 디렉토리 기준으로 수정
pwd
java -jar blink-batch/build/libs/blink-batch-0.0.1-SNAPSHOT.jar \
    --job.name=UserDataNotificationJobConfig \
    # --spring.profiles.active=local \
    >> "$LOG_FILE" 2>&1

if [ $? -eq 0 ]; then
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] Job completed successfully" >> "$LOG_FILE"
else
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] Job failed with error code $?" >> "$LOG_FILE"
fi