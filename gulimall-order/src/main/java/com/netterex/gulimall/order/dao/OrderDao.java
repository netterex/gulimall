package com.netterex.gulimall.order.dao;

import com.netterex.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author netterex
 * @email netterxu@gmail.com
 * @date 2024-05-17 15:19:53
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
