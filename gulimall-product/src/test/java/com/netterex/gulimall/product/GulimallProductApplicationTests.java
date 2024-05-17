package com.netterex.gulimall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.netterex.gulimall.product.entity.BrandEntity;
import com.netterex.gulimall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
