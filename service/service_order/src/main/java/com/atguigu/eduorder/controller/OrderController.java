package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-05-02
 */
@RestController
@RequestMapping("/eduorder/order")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    //生成订单
    @PostMapping("createOrder/{couseId}")
    public R saveOrder(@PathVariable String couseId, HttpServletRequest request) {

        //创建订单返回订单号
        String orderNo = orderService.createOrders(couseId,JwtUtils.getMemberIdByJwtToken(request));
        return R.ok().data("orderId",orderNo);
    }

    //根据订单id查询订单信息
    @GetMapping("getOrderInfo/{orderId}")
    public R getOrderInfo(@PathVariable String orderId) {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("order_no", orderId);
        Order serviceOne = orderService.getOne(orderQueryWrapper);
        return R.ok().data("item", serviceOne);
    }

    //根据课程id和用户id查询订单表中的状态
    @GetMapping("isByCourse/{courseId}/{memberId}")
    public boolean isByCourse(@PathVariable String courseId, @PathVariable String memberId) {

        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        wrapper.eq("member_id", memberId);
        wrapper.eq("status", 1);
        int count = orderService.count(wrapper);
        return count>0?true:false;
    }


}

