package com.netterex.gulimall.thirdparty;

import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class GulimallThirdpartyApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    MinioClient minioClient;

    @Test
    void testMinIO() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            // 检查桶是否存在，存在则使用，不存在则创建
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket("product").build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("product").build());
            } else {
                System.out.println("Bucket already exists.");
            }

            // 获取当前系统时间并格式化，以统一文件前缀
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDateTime = now.format(formatter);
            String dirPrefix = formattedDateTime + "/";

            String signedUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket("product")
                            .object(dirPrefix + "wukong.png")
                            .expiry(30, TimeUnit.SECONDS)
                            .build());
            System.out.println("signedUrl = " + signedUrl);

//            minioClient.uploadObject(
//                    UploadObjectArgs.builder()
//                            .bucket("product")
//                            .object(dirPrefix + "wukong.png")
//                            .filename("C:\\Users\\netterex\\Pictures\\Screenshots\\wukong.png")
//                            .build());
//            System.out.println("Upload complete.");
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }
    }

}
