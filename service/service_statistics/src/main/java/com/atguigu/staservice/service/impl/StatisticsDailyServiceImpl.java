package com.atguigu.staservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.staservice.client.UcenterClient;
import com.atguigu.staservice.entity.StatisticsDaily;
import com.atguigu.staservice.mapper.StatisticsDailyMapper;
import com.atguigu.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-05-07
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterClient ucenterClient;

    //统计某一天的注册人数,生成统计数据
    @Override
    public void registerCount(String day) {
        //添加记录删除表中相同的数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated",day);
        baseMapper.delete(wrapper);


        //远程调用用
        R r = ucenterClient.countRegister(day);
       Integer countRegister =(Integer) r.getData().get("countRegister");

       //把获取到的数据添加到数据库
        StatisticsDaily statisticsDaily = new StatisticsDaily();
        statisticsDaily.setRegisterNum(countRegister);//注册人数
        statisticsDaily.setDateCalculated(day);//统计日期
        statisticsDaily.setLoginNum(RandomUtils.nextInt(100, 200));
        statisticsDaily.setVideoViewNum( RandomUtils.nextInt(100, 200));
        statisticsDaily.setCourseNum( RandomUtils.nextInt(100, 200));
        baseMapper.insert(statisticsDaily);
    }

    //图标项显示 返回两个数组 日期json数组 数量 json数组
    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {

        //根据条件查询对应的数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated", begin, end);
        wrapper.select("date_calculated",type);
        List<StatisticsDaily> statisticsDailies = baseMapper.selectList(wrapper);

        //返回两部分数据 前端需要数组
        List<String> date_calculated = new ArrayList<>();
        List<Integer> numDataList = new ArrayList<>();
        
        //遍历查询所有数据list集合
        for (int i = 0; i < statisticsDailies.size(); i++) {
            StatisticsDaily daily = statisticsDailies.get(i);
            //封装
            date_calculated.add(daily.getDateCalculated());
            //封装对应数量
            switch (type) {
                case "login_num":
                    numDataList.add(daily.getLoginNum());
                    break;
                case "register_num":
                    numDataList.add(daily.getRegisterNum());
                    break;
                case "video_view_num":
                    numDataList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    numDataList.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }
        //封装之后的两个list集合梵高map中
        Map<String, Object> map = new HashMap<>();
        map.put("date_calculatedList",date_calculated);
        map.put("numDataList",numDataList);
        return map;
    }
}
