package com.netterex.gulimall.thirdparty.controller;

import com.netterex.common.utils.R;
import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.Map;

@RestController
public class MinioController {

    @Value("${minio.bucket}")
    private String bucket;

    @Value("${minio.endpoint}")
    private String endpoint;

    @Autowired
    private MinioClient minioClient;

    @RequestMapping ("/minio/policy")
    public R policy(@RequestParam("pic")String pic) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        Map<String, String> response = new LinkedHashMap<>();
        try {
            // 检查桶是否存在，存在则使用，不存在则创建
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            } else {
                System.out.println("Bucket already exists.");
            }

            Map<String, String> reqParams = new HashMap<>();
            reqParams.put("response-content-type", "application/json");

            // 获取当前系统时间并格式化，以统一文件前缀
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDateTime = now.format(formatter);
            String name = UUID.randomUUID() + "-" + pic;
            String dirPrefix = formattedDateTime + "/";
            pic = dirPrefix + name;
            String path = "/" + bucket + "/" + pic;

            String signedUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(bucket)
                            .object(pic)
                            .expiry(30, TimeUnit.SECONDS)
                            .extraQueryParams(reqParams)
                            .build());
            System.out.println("signedUrl = " + signedUrl);

            response.put("name", name);
            response.put("host", signedUrl);
            response.put("path", path);
            response.put("url", endpoint + path);
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
            response.put("error", e.getMessage());
        }
        return R.ok().put("data", response);
    }
}
