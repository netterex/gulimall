package com.netterex.gulimall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.netterex.gulimall.product.entity.BrandEntity;
import com.netterex.gulimall.product.service.BrandService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
class GulimallProductApplicationTests {

    @Autowired
    private BrandService brandService;

    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        //brandEntity.setDescript("good");
        //brandEntity.setName("一加");
        //brandService.save(brandEntity);
        //System.out.println("save success");
        BrandEntity brand = brandService.getOne(new QueryWrapper<>(brandEntity).eq("brand_id", 1));
        System.out.println(brand.toString());
    }

}
