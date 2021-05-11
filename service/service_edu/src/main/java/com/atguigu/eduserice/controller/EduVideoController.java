package com.atguigu.eduserice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduserice.client.VodClient;
import com.atguigu.eduserice.entity.EduVideo;
import com.atguigu.eduserice.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-04-01
 */
@RestController
@RequestMapping("/eduservice/video")
@CrossOrigin
public class EduVideoController {

    @Autowired
    private EduVideoService videoService;

    //注入vodClient
    @Autowired
    private VodClient vodClient;

    //添加小节
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo) {
        videoService.save(eduVideo);
        return R.ok();
    }

    //删除小节 删除对应的阿里云视频
    @DeleteMapping("{id}")
    public R deleteVideo(@PathVariable String id) {
        //根据小节id查询视频id
        EduVideo eduVideo = videoService.getById(id);
        String videoSourceId = eduVideo.getVideoSourceId();
        //判断小节中是否有id
        if(!StringUtils.isEmpty(videoSourceId)) {
            //根据视频id做到远程调用实现视频删除
            vodClient.removeAlyVideo(videoSourceId);
        }
        videoService.removeById(id);
        return R.ok();
    }

    //修改小节
    //修改章节(根据小节id查询)
    @GetMapping("getVideoInfo/{videoId}")
    public R getVideoInfo(@PathVariable String videoId) {
        EduVideo videoServiceById = videoService.getById(videoId);
        return  R.ok().data("video",videoServiceById );
    }

    //修改小节
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo) {
        videoService.updateById(eduVideo);
        return R.ok();
    }



}

