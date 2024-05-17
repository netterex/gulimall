package com.netterex.gulimall.coupon.dao;

import com.netterex.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author netterex
 * @email netterxu@gmail.com
 * @date 2024-05-17 15:01:21
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
