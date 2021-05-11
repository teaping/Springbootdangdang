package com.atguigu.msmservice.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.msmservice.service.MsmService;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

import static com.atguigu.msmservice.utils.RestTest.testSendSms;

@Service
public class MsmServiceIpml  implements MsmService {

    //发送短信方法
    @Override
    public Boolean send(String code, String phone) {

        if(StringUtils.isEmpty(phone)) return false;
        //        手动加载spring环境
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext*.xml");
//        根据上下文 从spring环境获取创建邮件发送对象
        JavaMailSender mailSender = (JavaMailSender)context.getBean("mailSend");
//        1.发送最简单邮件
        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        设置邮件属性  发送者邮件地址
        mailMessage.setFrom("small_xia@126.com");
//        设置发给谁 邮件接收者
        mailMessage.setTo(phone);
//        设置邮件标题
        mailMessage.setSubject("[楠威]");
//        设置邮件正文
        mailMessage.setText("尊敬的楠威用户您好！！！验证码是："+code+"，5分钟有效");
//        调用邮件发送对象，发送邮件
        mailSender.send(mailMessage);
        System.out.println("邮件发送成功");
//        DefaultProfile profile =
//        DefaultProfile.getProfile("default", "LTAI5tJiuq3qa1BnF6XA8dpu", "ObsCG6UwtGuXqYtcg00fOXjZNWrXDa");
//        IAcsClient client = new DefaultAcsClient(profile);
//
//        //设置相关参数
//        CommonRequest request = new CommonRequest();
//        //request.setProtocol(ProtocolType.HTTPS);
//        request.setMethod(MethodType.POST);
//        request.setDomain("dysmsapi.aliyuncs.com");
//        request.setVersion("2017-05-25");
//        request.setAction("SendSms");
//
//        //设置发送相关参数
//        request.putQueryParameter("PhoneNumbers",phone); //发送手机号
//        request.putQueryParameter("SignName","我的谷粒教育网站"); //申请阿里云签名名称

//        Boolean aBoolean = testSendSms(sid, token, appid, templateid, code, phone,"2d92c6132139467b989d087c84a365d8");
        return true;
    }
}
