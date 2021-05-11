package com.atguigu.educenter.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.educenter.utils.ConstantWxUtils;
import com.atguigu.educenter.utils.HttpClientUtils;
import com.atguigu.sericebase.exceptionhandler.GuliException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@CrossOrigin
@Controller//注意这里没有配置@RestController
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    @Autowired
    private UcenterMemberService ucenterMemberService;


    //获取扫描人信息，添加数据
    @GetMapping("callback")
    public String callback(String code, String state) {
        //获取coud值 临时票据
        //拿code请求微信固定地址 得到 token 和 openid
        //向认证服务器发送请求换取access_token
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
        "?appid=%s" +
        "&secret=%s" +
        "&code=%s" +
        "&grant_type=authorization_code";
        

        //拼接桑参数
        String accssTokenUrl = String.format(baseAccessTokenUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                ConstantWxUtils.WX_OPEN_APP_SECRET,
                code);
        //请求拼接好的地址
        //使用http发送请求 得到返回结果
        String accessTokenInfo = null;
        try {
             accessTokenInfo = HttpClientUtils.get(accssTokenUrl);
        } catch (Exception e) {
            throw new GuliException(20001,"登录失败");
        }

        //从accessTokenInfo中取出两个值
        //把accessTokenInfo转换map 根据map key取值
        //使用json转换工具 GSON
        Gson gson = new Gson();
        HashMap mapAccess = gson.fromJson(accessTokenInfo, HashMap.class);
        String access_token = (String)mapAccess.get("access_token");
        String openid = (String)mapAccess.get("openid");


        //扫描信息添加数据库中
        //判断数据库里面是否存在相同信息 根据openid判断
        UcenterMember member = ucenterMemberService.getOpenIdMember(openid);
        if (member == null) {
         //对象不是空 进行添加

            //拿着token openid 去请求微信固定地址 得到扫描人信息
            //访问微信的资源服务器，获取用户信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";
            //拼接
            String userInfoUrl = String.format(baseUserInfoUrl,
                    access_token,
                    openid);

            //发送请求
            String userInfo = null;
            try {
                userInfo = HttpClientUtils.get(userInfoUrl);
            } catch (Exception e) {
                throw new GuliException(20001,"登录失败");
            }
            //获取返回userInfo用户信息
            HashMap userMap = gson.fromJson(userInfo, HashMap.class);
            String nickname = (String)userMap.get("nickname");
            String headimgurl = (String)userMap.get("headimgurl");

            member = new UcenterMember();
            member.setOpenid(openid);
            member.setNickname(nickname);
            member.setAvatar(headimgurl);
            ucenterMemberService.save(member);
        }
        //返回首页面
        //使用jwt根据member生成token字符穿
        String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());


        //通过路径传递
        return "redirect:http://localhost:3000?token="+jwtToken;
    }


    //生成微信扫描二维码
    @GetMapping("login")
    public String getWcCode() {
        //固定地址拼接参数
//        String url = "https://open.weixin.qq.com/" +
//                "connect/qrconnect?appod="+ ConstantWxUtils.WX_OPEN_APP_ID+"response_type=";

        // 微信开放平台授权baseUrl

        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        //对redirect_url 进行URLEncoder 编码
        String redirect_url = ConstantWxUtils.WX_OPEN_REDIRECT_URL;
        try {
            redirect_url = URLEncoder.encode(redirect_url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //设%@s里面的值
        String url = String.format(baseUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                ConstantWxUtils.WX_OPEN_REDIRECT_URL,
                redirect_url,
                "atguigu"
        );
        //请求微信地址

        return "redirect:"+url;
    }
 }
