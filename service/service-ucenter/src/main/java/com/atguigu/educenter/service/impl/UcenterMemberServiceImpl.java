package com.atguigu.educenter.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.mapper.UcenterMemberMapper;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.sericebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-04-12
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    //登入方法
    @Override
    public String login(UcenterMember member) {
        String mobile = member.getMobile();
        String password = member.getPassword();

        //手机号判断非空
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new GuliException(20001,"登录失败");
        }

        //判断手机号是否为正确
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        UcenterMember mobileMember = baseMapper.selectOne(wrapper);
        //判断查出来的对象是否为空
        if (mobileMember == null) {
            //没有使用手机号
            throw new GuliException(20001,"登录失败");
        }

        //判断密码
        //因为密码是加密的
        //把输入的密码进行加密，再进行对比
        if (!MD5.encrypt(password).equals(mobileMember.getPassword())) {
            throw new GuliException(20001,"密码错误");
        }
        //判断账号是否禁用
        if (mobileMember.getIsDisabled()) {
            throw new GuliException(20001,"账号已被禁用");
        }
        //登入成功
        //生成token 使用jwt
        String jwtToken = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());

        return jwtToken;
    }

    //注册
    @Override
    public void register(RegisterVo registerVo) {
        //获取注册数据
        String code = registerVo.getCode(); //验证码
        String mobile = registerVo.getMobile();//手机号
        String nickname = registerVo.getNickname();//昵称
        String password = registerVo.getPassword();//密码

        //判断非空
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)
                ||StringUtils.isEmpty(nickname) || StringUtils.isEmpty(code) ) {
            throw new GuliException(20001,"注册失败");
        }

        //验证手机验证码是否正确
        String redisCode = redisTemplate.opsForValue().get(mobile+"@126.com");
        if (!code.equals(redisCode)) {
            throw new GuliException(20001,"注册失败");
        }

        //手机号是否重复
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer selectCount = baseMapper.selectCount(wrapper);
        if (selectCount > 0) {
            throw new GuliException(20001,"注册失败");
        }

        //数据添加
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);
        member.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");
        baseMapper.insert(member);

    }

    //判断数据库里面是否存在相同信息 根据openid判断
    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }

    //查询某一天的注册人数
    @Override
    public Integer countRegister(String day) {
        return baseMapper.countRegister(day);
    }
}
