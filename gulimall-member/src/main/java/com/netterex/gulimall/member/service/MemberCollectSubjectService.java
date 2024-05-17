package com.netterex.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.netterex.common.utils.PageUtils;
import com.netterex.gulimall.member.entity.MemberCollectSubjectEntity;

import java.util.Map;

/**
 * 会员收藏的专题活动
 *
 * @author netterex
 * @email netterxu@gmail.com
 * @date 2024-05-17 15:09:10
 */
public interface MemberCollectSubjectService extends IService<MemberCollectSubjectEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

