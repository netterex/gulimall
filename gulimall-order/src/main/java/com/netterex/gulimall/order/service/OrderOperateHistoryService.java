package com.netterex.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.netterex.common.utils.PageUtils;
import com.netterex.gulimall.order.entity.OrderOperateHistoryEntity;

import java.util.Map;

/**
 * 订单操作历史记录
 *
 * @author netterex
 * @email netterxu@gmail.com
 * @date 2024-05-17 15:19:53
 */
public interface OrderOperateHistoryService extends IService<OrderOperateHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

