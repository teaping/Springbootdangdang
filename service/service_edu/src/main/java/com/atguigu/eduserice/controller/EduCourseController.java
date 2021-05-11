package com.atguigu.eduserice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduserice.entity.EduCourse;
import com.atguigu.eduserice.entity.EduTeacher;
import com.atguigu.eduserice.entity.vo.CourseInfoVo;
import com.atguigu.eduserice.entity.vo.CoursePublishVo;
import com.atguigu.eduserice.entity.vo.TeacherQuery;
import com.atguigu.eduserice.service.EduCourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-04-01
 */
@RestController
@RequestMapping("/eduservice/edu-course")
@CrossOrigin
public class EduCourseController {
    @Autowired
    private EduCourseService courseService;


    //课程列表分页
    @PostMapping("pageCouruseCondition/{current}/{limlt}")
    public R pageTeacherCondition(@PathVariable long current,
                                  @PathVariable long limlt,
                                  @RequestBody(required = false) EduCourse eduCourse){

        Page<EduCourse> pageCourse = new Page<>(current,limlt);

        //构建条件
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        //多条件组合查询 动态sql
        String title = eduCourse.getTitle();
        String status = eduCourse.getStatus();
        if(!StringUtils.isEmpty(title)) {
            wrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(status)) {
            wrapper.eq("status",status);
        }
        //根据时间排序
        wrapper.orderByDesc("gmt_create");
        courseService.page(pageCourse,wrapper);
        long total = pageCourse.getTotal();//总记录数
        List<EduCourse> records = pageCourse.getRecords();
        return  R.ok().data("total",total).data("rows", records);
    }






    //添加课程基本信息的方法
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {

        //返回添加之后课程id 为了后面添加大纲使用
        String id = courseService.saveCourseInfo(courseInfoVo);

        return  R.ok().data("courseId",id);
    }

    //根据课程id查询课程信息
    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId) {

        CourseInfoVo courseInfoVo =  courseService.getCourseInfo(courseId);

        return R.ok().data("courseInfoVo",courseInfoVo);
    }

    //修改课程信息
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    //根据课程id查询课程确认信息
    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id) {
        CoursePublishVo coursePublishVo =  courseService.pulishCourseInfo(id);
        return  R.ok().data("publishCourse",coursePublishVo);
    }


    //课程最终发布
    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id) {
        EduCourse eduCourse=new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal"); //设置课程发布状态
        courseService.updateById(eduCourse);
        return R.ok();
    }

    //删除课程
    @DeleteMapping("{courseId}")
    public R deleteCourse(@PathVariable String courseId) {
        courseService.removeCourse(courseId);
        return R.ok();
    }



}

