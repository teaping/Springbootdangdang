package com.atguigu.msmservice.service;

import java.util.Map;

public interface MsmService {
    //发送短信方法
    Boolean send(String code, String phone);
}
