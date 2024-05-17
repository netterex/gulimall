package com.netterex.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.netterex.common.utils.PageUtils;
import com.netterex.gulimall.coupon.entity.HomeSubjectEntity;

import java.util.Map;

/**
 * 首页专题表【jd首页下面很多专题，每个专题链接新的页面，展示专题商品信息】
 *
 * @author netterex
 * @email netterxu@gmail.com
 * @date 2024-05-17 15:01:21
 */
public interface HomeSubjectService extends IService<HomeSubjectEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

