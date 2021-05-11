package com.atguigu.eduserice.service;

import com.atguigu.eduserice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-03-23
 */
public interface EduTeacherService extends IService<EduTeacher> {

    //查询热门四位老师
    List<EduTeacher> selectList();

    // //分页查询
    Map<String,Object> getTeacherFrontList(Page<EduTeacher> teacherPage);
}
