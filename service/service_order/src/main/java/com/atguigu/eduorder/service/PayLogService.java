package com.atguigu.eduorder.service;

import com.atguigu.eduorder.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-05-02
 */
public interface PayLogService extends IService<PayLog> {

    //生成二维码接口
    Map createNative(String orderNo);

    //根据订单号查询支付状态
    Map<String,String> querPayStatus(String orderNo);

    //向支付表添加支付记录 更新支付状态
    void updateOrederState(Map<String, String> map);
}
