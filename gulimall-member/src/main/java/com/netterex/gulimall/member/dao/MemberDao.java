package com.netterex.gulimall.member.dao;

import com.netterex.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author netterex
 * @email netterxu@gmail.com
 * @date 2024-05-17 15:09:10
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
