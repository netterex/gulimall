package com.netterex.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.netterex.common.utils.PageUtils;
import com.netterex.gulimall.coupon.entity.SeckillSkuNoticeEntity;

import java.util.Map;

/**
 * 秒杀商品通知订阅
 *
 * @author netterex
 * @email netterxu@gmail.com
 * @date 2024-05-17 15:01:21
 */
public interface SeckillSkuNoticeService extends IService<SeckillSkuNoticeEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

