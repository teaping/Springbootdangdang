package com.atguigu.educenter.service;

import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-04-12
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    //登入方法
    String login(UcenterMember member);

    //注册
    void register(RegisterVo registerVo);

    //判断数据库里面是否存在相同信息 根据openid判断
    UcenterMember getOpenIdMember(String openid);

    //查询某一天的注册人数
    Integer countRegister(String day);
}
