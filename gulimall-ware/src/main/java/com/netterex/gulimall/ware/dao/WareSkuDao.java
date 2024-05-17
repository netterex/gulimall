package com.netterex.gulimall.ware.dao;

import com.netterex.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品库存
 * 
 * @author netterex
 * @email netterxu@gmail.com
 * @date 2024-05-17 15:23:39
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
	
}
