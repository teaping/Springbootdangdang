package com.atguigu.educenter.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.ordervo.UcenterMemberOrder;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.service.UcenterMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-04-12
 */
@RestController
@RequestMapping("/educenter/member")
@CrossOrigin
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;

    //登入
    @PostMapping("login")
    public R loginUser(@RequestBody UcenterMember member){
        //meder对象封装手机号
        //调用service方法登入
        String token = memberService.login(member);
        return R.ok().data("token",token);
    }

    //注册
    @PostMapping("register")
    public R registerUser(@RequestBody RegisterVo registerVo) {

        memberService.register(registerVo);

        return R.ok();
    }

    //创建接口根据token获取用户信息
    @GetMapping("getUserInfo")
    public R getUserInfo(HttpServletRequest request) {
        //调用jwt工具类的方法 根据requst对象获取头信息 返回id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //哈希数据库根据id获取用户信息
        UcenterMember member = memberService.getById(memberId);
        return R.ok().data("userInfo",member);
    }

    //根据token字符串获取用户信息
    @PostMapping("getInfoUc/{id}")
    public R getInfo(@PathVariable String id) {

        //根据用户id获取用户信息

        UcenterMember ucenterMember = memberService.getById(id);

        UcenterMember  memeber = new UcenterMember();

        BeanUtils.copyProperties(ucenterMember,memeber);

        String memeberId = memeber.getId();
        String memeberNickname = memeber.getNickname();
        String avatar = memeber.getAvatar();
        List<String> list = new ArrayList<>();
        list.add(memeberId);
        list.add(memeberNickname);
        list.add(avatar);

        return R.ok().data("list",list);
    }

    //根据用户id查询用户信息
    @PostMapping("getUserInfoOrder/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String id) {
        UcenterMember memberServiceById = memberService.getById(id);
        //把UcenterMember赋值给UcenterMemberOrder对象
        UcenterMemberOrder ucenterMemberOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(memberServiceById,ucenterMemberOrder);
        return ucenterMemberOrder;
    }

    //查询某一天的注册人数
    @GetMapping("countRegister/{day}")
    public R countRegister(@PathVariable String day) {
    Integer count = memberService.countRegister(day);
        return R.ok().data("countRegister",count);
    }


}

