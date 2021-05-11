package com.atguigu.eduserice.client;

import com.atguigu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name="service-ucenter",fallback = CommDegradClint.class)
public interface CommClient {
    //根据用户id获取用户信息
    @GetMapping("/educenter/member/getUcenterPay/{memberId}")
    public R getUcenterPay(@PathVariable("memberId") String memberId);
}
