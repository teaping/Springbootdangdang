package com.atguigu.eduorder.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.entity.PayLog;
import com.atguigu.eduorder.mapper.PayLogMapper;
import com.atguigu.eduorder.service.OrderService;
import com.atguigu.eduorder.service.PayLogService;
import com.atguigu.eduorder.utils.HttpClient;
import com.atguigu.sericebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-05-02
 */
@Service
public class  PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderService orderService;
    //生成二维码接口
    @Override
    public Map createNative(String orderNo) {

        try{
            //根据订单号查询订单信息
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no", orderNo);
            Order order = orderService.getOne(wrapper);
            //使用集合map 生成二维码参数
            Map m = new HashMap();
            m.put("appid", "wx74862e0dfcf69954");

            m.put("mch_id", "1558950191");

            m.put("nonce_str", WXPayUtil.generateNonceStr());

            m.put("body", order.getCourseTitle());

            m.put("out_trade_no", orderNo);

            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");

            m.put("spbill_create_ip", "127.0.0.1");

            m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify");

            m.put("trade_type", "NATIVE");


            //发送http请求 传参格式xml
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");

            //设置xml格式参数
            client.setXmlParam(WXPayUtil.generateSignedXml(m,"T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            //执行请求发送
            client.post();
            //得到发送请求的结果 返回内容是xml格式
            String content = client.getContent();
            //把xml转换成map集合
            Map<String, String> resultMap = WXPayUtil.xmlToMap(content);

            //最终返回数据的封装
            Map map = new HashMap();
            map.put("out_trade_no", orderNo);

            map.put("course_id", order.getCourseId());

            map.put("total_fee", order.getTotalFee());

            map.put("result_code", resultMap.get("result_code")); //返回二维码操作的状态码

            map.put("code_url", resultMap.get("code_url")); //二维码的地址

            return map;

        }catch (Exception e) {
        throw new GuliException(20001,"生成二维码失败");
        }
    }

    //根据订单号查询支付状态
    @Override
    public Map<String, String> querPayStatus(String orderNo) {

        try{
            Map m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            //发送httpclint请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m,"T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();

            //得到内容
            String xml =client.getContent();
            Map<String,String> resultMap= WXPayUtil.xmlToMap(xml);
            return resultMap;

        }catch (Exception e) {
            throw  new  GuliException(20001,"获取状态错误");
        }
    }


    //向支付表添加支付记录 更新支付状态
    @Override
    public void updateOrederState(Map<String, String> map) {

        //从map获取打订单号
        String orderNo = map.get("out_trade_no");
        //根据订单号查询订单信息
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);
        Order order = orderService.getOne(wrapper);

        //更新订单表状态
        if(order.getStatus().intValue() == 1) { return; }

        order.setStatus(1); //1代表已支付
        orderService.updateById(order);

        //向支付表添加支付记录
        PayLog payLog = new PayLog();
        payLog.setOrderNo(orderNo);//支付订单号
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));
        payLog.setAttr(JSONObject.toJSONString(map));
        baseMapper.insert(payLog);//插入到支付日志表

    }
}
