package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduorder.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-05-02
 */
@RestController
@RequestMapping("/eduorder/paylog")
@CrossOrigin
public class PayLogController {

    @Autowired
    private PayLogService payLogService;

    //生成二维码接口 参数订单号
    @GetMapping("createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo) {
        //返回包含二维码地址 还有其他信息
        Map map = payLogService.createNative(orderNo);
        return R.ok().data(map);
    }

    //查询订单支付状态 参数：订单号 查询支付状态
    @GetMapping("querPayStatus/{orderNo}")
    public R querPayStatus(@PathVariable String orderNo) {
       Map<String,String> map =  payLogService.querPayStatus(orderNo);

       if (map == null) {
           return R.err().message("支持出错");
       }
       //如果map 不为空
        if (map.get("trade_state").equals("SUCCESS")) { //支付成功
            //添加记录到支付表中 更新订单表状态
            payLogService.updateOrederState(map);
            return R.ok().message("支付成功");
        }
        return R.ok().code(25000).message("支付中");
    }
}

