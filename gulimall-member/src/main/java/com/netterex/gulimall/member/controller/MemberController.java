package com.netterex.gulimall.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.netterex.gulimall.member.feign.OrderFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.netterex.gulimall.member.entity.MemberEntity;
import com.netterex.gulimall.member.service.MemberService;
import com.netterex.common.utils.PageUtils;
import com.netterex.common.utils.R;



/**
 * 会员
 *
 * @author netterex
 * @email netterxu@gmail.com
 * @date 2024-05-17 15:09:10
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private OrderFeignService orderFeignService;

    @RequestMapping("/list/orders")
    public R orderslist() {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname("王五");

        R orders = orderFeignService.listmember();

        return R.ok().put("member", memberEntity).put("orders", orders.get("orders"));

    }

    /**
     * 列表
     */
    @RequestMapping("/list")
        public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
        public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
        public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
        public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
        public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
