package com.atguigu.eduserice.mapper;

import com.atguigu.eduserice.entity.EduCourse;
import com.atguigu.eduserice.entity.frontvo.CourseWebVo;
import com.atguigu.eduserice.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2021-04-01
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {


        public CoursePublishVo getPublishCourseInfo(String courseId);


    //根据课程id
    CourseWebVo getBaseCoureInfo(String coursId);
}
