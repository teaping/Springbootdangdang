package com.atguigu.eduserice.client;

import com.atguigu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "service-vod",fallback = VodFileDegradeFeignClient.class) //调用的服务名
@Component
public interface VodClient {

    //定义调用方法路径
    //根据视频id删除阿里云中的视频
    @DeleteMapping("/eduvod/video/removeAlyVideo/{id}")
    public R removeAlyVideo(@PathVariable("id") String id);



    //定义删除多个视频的方法
    @DeleteMapping("/eduvod/video/delete-batch")
    public R deletebatch(@RequestParam("videoIdList") List<String> videoIdList);



    }
