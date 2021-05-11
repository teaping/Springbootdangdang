package com.atguigu.eduserice.service;

import com.atguigu.eduserice.entity.EduCourse;
import com.atguigu.eduserice.entity.frontvo.CourseFronVo;
import com.atguigu.eduserice.entity.frontvo.CourseWebVo;
import com.atguigu.eduserice.entity.vo.CourseInfoVo;
import com.atguigu.eduserice.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-04-01
 */
public interface EduCourseService extends IService<EduCourse> {
    //添加课程基本信息的方法
    String saveCourseInfo(CourseInfoVo courseInfoVo);


    //根据课程id查询课程信息
    CourseInfoVo getCourseInfo(String courseId);


    //修改课程信息
    void updateCourseInfo(CourseInfoVo courseInfoVo);


    //根据课程id查询课程确认信息
    CoursePublishVo pulishCourseInfo(String id);


    //删除课程
    void removeCourse(String courseId);

    //查询前八条热门课程，
    List<EduCourse> selctList();

    //条件查询带分页查询数据
    Map<String,Object> getCourseFrontList(Page<EduCourse> eduCoursePage,CourseFronVo courseFronVo);

    //根据课程id
    CourseWebVo getBaseCouseInfo(String coursId);
}
