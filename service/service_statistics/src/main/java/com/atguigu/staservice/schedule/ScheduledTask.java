package com.atguigu.staservice.schedule;

import com.atguigu.staservice.service.StatisticsDailyService;
import com.atguigu.staservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {

    @Autowired
    private StatisticsDailyService dailyService;

    /**
     * 每天凌晨1点执行定时
     */

    @Scheduled(cron = "0 0 1 * * ?")
    public void task2() {
        //获取上一天的日期
        dailyService.registerCount(DateUtil.formatDate(DateUtil.addDays(new Date(), -1)));

    }

}
