package com.atguigu.msmservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.msmservice.service.MsmService;
import com.atguigu.msmservice.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("edumsm/msm")
@CrossOrigin
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //发送短信的方法
    @PostMapping("send")
    public R sendMsm(@RequestBody String phone) {

        //从redis获取验证码
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)){
            return R.ok();
        }
        //取不到进行阿里云发送

        //生成随机值
        code = RandomUtil.getFourBitRandom();
//        Map<String, Object> param = new HashMap<>();
//        param.put("code",code);

        System.out.println(phone+"-------------phone");

        String ipon=phone.replace("=", "")+"@126.com";
        //调用service发送短信方法
       Boolean isSend = msmService.send(code,ipon);

       if (isSend){
            //发送成功把验证码梵高redis
           //设置有效时间
           redisTemplate.opsForValue().set(ipon,code,5,TimeUnit.MINUTES);
           return R.ok();
       }else{
           return R.err().message("短信发送失败");
       }
    }
}
