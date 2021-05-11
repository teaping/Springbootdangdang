package com.atguigu.eduserice.service;

import com.atguigu.eduserice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-04-01
 */
public interface EduVideoService extends IService<EduVideo> {


    //根据课程id删除小节
    void removeVideoByCourseId(String courseId);
}
