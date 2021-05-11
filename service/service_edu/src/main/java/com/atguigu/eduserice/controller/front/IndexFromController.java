package com.atguigu.eduserice.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.eduserice.entity.EduCourse;
import com.atguigu.eduserice.entity.EduTeacher;
import com.atguigu.eduserice.service.EduCourseService;
import com.atguigu.eduserice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eduservice/indexfront")
@CrossOrigin
public class IndexFromController {


    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduTeacherService teacherService;
    //查询前八条热门课程，查询前四条名师
    @GetMapping("index")
    public R index() {

        //查询前八条热门课程，
        List<EduCourse> eduList = courseService.selctList();

        //查询前四条名师
        List<EduTeacher> teacherList = teacherService.selectList();

        return R.ok().data("eduList",eduList).data("teacherList",teacherList);
    }



}
