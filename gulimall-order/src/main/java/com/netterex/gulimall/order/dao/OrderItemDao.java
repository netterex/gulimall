package com.netterex.gulimall.order.dao;

import com.netterex.gulimall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author netterex
 * @email netterxu@gmail.com
 * @date 2024-05-17 15:19:53
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
