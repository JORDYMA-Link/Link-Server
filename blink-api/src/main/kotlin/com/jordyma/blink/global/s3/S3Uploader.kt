package com.jordyma.blink.global.s3

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.PutObjectRequest
import com.jordyma.blink.feed.entity.Feed
import com.jordyma.blink.image.entity.Image
import com.jordyma.blink.logger
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


@Slf4j
@RequiredArgsConstructor
@Component
class S3Uploader (
     private val amazonS3: AmazonS3,
     @Value("\${spring.cloud.aws.s3.bucket}") private val bucket: String
){
    // 피드 썸네일 이미지 업로드
    @Throws(IOException::class)
    fun s3UploadOfThumbnailImage(feed: Feed, multipartFile: MultipartFile): String {

        //폴더 경로
        val folderPath = "thumbnail"

        //파일 이름
        val frontName: String = java.lang.String.valueOf(feed.id)
        val fileName = createFileName(frontName, multipartFile.originalFilename)
        return s3Upload(folderPath, fileName, multipartFile)
    }

    /**
     * S3 업로드
     */
    @Throws(IOException::class)
    private fun s3Upload(folderPath: String, fileNm: String, multipartFile: MultipartFile): String {
        val uploadFile = convert(multipartFile) // 파일 변환할 수 없으면 에러
            .orElseThrow {
                IllegalArgumentException(
                    "error: MultipartFile -> File convert fail"
                )
            }

        //S3에 저장될 위치 + 저장파일명
        val storeKey = "$folderPath/$fileNm"

        //s3로 업로드
        val imageUrl = putS3(uploadFile, storeKey)

        //File 로 전환되면서 로컬에 생성된 파일을 제거
        removeNewFile(uploadFile)
        return imageUrl
    }

    /**
     * S3 이미지 삭제
     */
    fun delete(image: Image) {
        try {
            val imageUrl: String = image.url
            val storeKey = imageUrl.replace("https://$bucket.s3.ap-northeast-2.amazonaws.com/", "")
            println("imageUrl: $imageUrl")
            println("storeKey: $storeKey")
            amazonS3!!.deleteObject(DeleteObjectRequest(bucket, storeKey))
        } catch (e: Exception) {
            logger().error("delete file error" + e.message)
        }
    }

    //S3 업로드
    private fun putS3(uploadFile: File, storeKey: String): String {
        amazonS3!!.putObject(
            PutObjectRequest(
                bucket,
                storeKey,
                uploadFile
            )
                // .withCannedAcl(CannedAccessControlList.PublicRead)
        )
        return amazonS3.getUrl(bucket, storeKey).toString()
    }

    // 로컬에 저장된 이미지 지우기
    private fun removeNewFile(targetFile: File) {
        if (targetFile.delete()) {
            logger().info("파일이 삭제되었습니다.")
        } else {
            logger().info("파일이 삭제되지 못했습니다.")
        }
    }

    // 로컬에 파일 업로드 하기
    @Throws(IOException::class)
    private fun convert(multipartFile: MultipartFile): Optional<File> {

        //파일 이름
        val originalFilename = multipartFile.originalFilename

        //파일 저장 이름
        val storeFileName = UUID.randomUUID().toString() + "_" + originalFilename
        val convertFile = File(System.getProperty("user.dir") + "/" + storeFileName)
        if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            FileOutputStream(convertFile).use { fos ->  // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(multipartFile.bytes)
            }
            return Optional.of(convertFile)
        }
        return Optional.empty()
    }

    private fun createFileName(frontName: String, originalFileName: String?): String {
        val uuid = UUID.randomUUID().toString()
        return frontName + "_" + uuid + "_" + originalFileName
    }

}