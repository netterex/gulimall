package com.netterex.gulimall.member.feign;

import com.netterex.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: neTTerex
 * @Description:
 * @DateTime: 2024/5/17 17:22
 **/

@FeignClient("gulimall-order")
public interface OrderFeignService {

    @RequestMapping("/order/order/list/member")
    public R listmember();
}
