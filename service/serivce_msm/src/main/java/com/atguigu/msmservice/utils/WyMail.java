package com.atguigu.msmservice.utils;


import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class WyMail {
    public static void main(String[] args) {
//        手动加载spring环境
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext*.xml");
//        根据上下文 从spring环境获取创建邮件发送对象
        JavaMailSender mailSender = (JavaMailSender)context.getBean("mailSend");
//        1.发送最简单邮件
        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        设置邮件属性  发送者邮件地址
        mailMessage.setFrom("small_xia@126.com");
//        设置发给谁 邮件接收者
        mailMessage.setTo("small_xia@126.com");
//        设置邮件标题
        mailMessage.setSubject("[楠威]");
//        设置邮件正文
        mailMessage.setText("尊敬的楠威用户您好！！！验证码是：12312，5分钟有效");
//        调用邮件发送对象，发送邮件
        mailSender.send(mailMessage);
        System.out.println("邮件发送成功");

    }
}
