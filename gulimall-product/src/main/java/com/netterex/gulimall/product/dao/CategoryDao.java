package com.netterex.gulimall.product.dao;

import com.netterex.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author netterex
 * @email netterxu@gmail.com
 * @date 2024-05-17 13:37:22
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
