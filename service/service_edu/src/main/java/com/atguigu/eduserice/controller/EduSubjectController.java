package com.atguigu.eduserice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduserice.entity.subject.OneSubject;
import com.atguigu.eduserice.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-03-31
 */
@RestController
@RequestMapping("/eduservice/subject")
@CrossOrigin
public class EduSubjectController {


    @Autowired
    private EduSubjectService subjectService;

    //添加课程分类
    //获取上传的文件 把文件内容读取出来、
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file) {
        //上传过来的exel文件
        subjectService.saveSubject(file,subjectService);
        return R.ok();
    }

    //课程分类列表功能
    @GetMapping("getAllSubject")
    public R getAllSubject() {
        //list集合中是一级分类   当中有它本身还有二级分类
       List<OneSubject> list = subjectService.getAllOnTwoSubject();
        return R.ok().data("list",list);
    }

}

