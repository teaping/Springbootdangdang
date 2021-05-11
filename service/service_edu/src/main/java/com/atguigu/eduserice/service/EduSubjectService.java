package com.atguigu.eduserice.service;

import com.atguigu.eduserice.entity.EduSubject;
import com.atguigu.eduserice.entity.subject.OneSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-03-31
 */
public interface EduSubjectService extends IService<EduSubject> {

    //添加课程分类
    void saveSubject(MultipartFile file,EduSubjectService subjectService);

    //课程分类列表功能(树形)
    List<OneSubject> getAllOnTwoSubject();
}
