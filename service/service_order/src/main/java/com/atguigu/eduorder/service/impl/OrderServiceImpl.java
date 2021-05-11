package com.atguigu.eduorder.service.impl;

import com.atguigu.commonutils.ordervo.CourseWebVoOrder;
import com.atguigu.commonutils.ordervo.UcenterMemberOrder;
import com.atguigu.eduorder.client.EduClient;
import com.atguigu.eduorder.client.UcenterClient;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.mapper.OrderMapper;
import com.atguigu.eduorder.service.OrderService;
import com.atguigu.eduorder.utils.OrderNoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-05-02
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private EduClient eduClient;

    @Autowired
    private UcenterClient ucenterClient;
    //创建订单返回订单号
    @Override
    public String createOrders(String couseId, String memberId) {
        //通过远程调用获取用户信息
        UcenterMemberOrder userInfoOrder = ucenterClient.getUserInfoOrder(memberId);

        //获取课程信息
        CourseWebVoOrder couseInfoOrder = eduClient.getCouseInfoOrder(couseId);

        //创建order对象 向order对象设置数据

        Order order =new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo());
        order.setCourseId(couseId);
        order.setCourseTitle(couseInfoOrder.getTitle());
        order.setCourseCover(couseInfoOrder.getCover());
        order.setTeacherName(couseInfoOrder.getTeacherName());
        order.setTotalFee(couseInfoOrder.getPrice());
        order.setMemberId(memberId);
        order.setMobile(userInfoOrder.getMobile());
        order.setNickname(userInfoOrder.getNickname());
        order.setStatus(0); //支付状态
        order.setPayType(1); //支付类型
        baseMapper.insert(order);

        //返回订单号
        return order.getOrderNo();
    }
}
