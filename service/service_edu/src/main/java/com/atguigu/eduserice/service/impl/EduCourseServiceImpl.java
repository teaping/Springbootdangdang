package com.atguigu.eduserice.service.impl;

import com.atguigu.eduserice.entity.EduCourse;
import com.atguigu.eduserice.entity.EduCourseDescription;
import com.atguigu.eduserice.entity.frontvo.CourseFronVo;
import com.atguigu.eduserice.entity.frontvo.CourseWebVo;
import com.atguigu.eduserice.entity.vo.CourseInfoVo;
import com.atguigu.eduserice.entity.vo.CoursePublishVo;
import com.atguigu.eduserice.mapper.EduCourseMapper;
import com.atguigu.eduserice.service.EduChapterService;
import com.atguigu.eduserice.service.EduCourseDescriptionService;
import com.atguigu.eduserice.service.EduCourseService;
import com.atguigu.eduserice.service.EduVideoService;
import com.atguigu.sericebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-04-01
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    //课程描述注入
    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    //注入小节和章节的serice
    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private EduChapterService eduChapterService;

    //添加课程基本信息的方法
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //向课程表添加课程基本信息
        //把CourseInfo对象转换eduCourse
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int insert = baseMapper.insert(eduCourse);

        if (insert ==0){
            //添加失败
            throw new GuliException(20001,"添加课程信息失败");
        }

        //向课程简介添加课程简介
        //edu_course_description

        String cid = eduCourse.getId();
        EduCourseDescription courseDescription = new EduCourseDescription();

        courseDescription.setDescription(courseInfoVo.getDescription());

        courseDescription.setId(cid);
        courseDescriptionService.save(courseDescription);
        return cid;
    }


    //根据课程id查询课程信息
    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        //查询课程表
        EduCourse eduCourse = baseMapper.selectById(courseId);

        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse,courseInfoVo);

        //查询课程描述表
        EduCourseDescription courseDescription = courseDescriptionService.getById(courseId);
        courseInfoVo.setDescription(courseDescription.getDescription());
        return courseInfoVo;
    }


    //修改课程信息
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        //修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int updateById = baseMapper.updateById(eduCourse);
        if (updateById == 0 ){
            throw  new GuliException(20001,"修改课程信息失败");
        }

        //修改描述表
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoVo,eduCourseDescription);
        courseDescriptionService.updateById(eduCourseDescription);

    }


    //根据课程id查询课程确认信息
    @Override
    public CoursePublishVo pulishCourseInfo(String id) {
        //调用mapper
        CoursePublishVo publishCourseInfo = baseMapper.getPublishCourseInfo(id);
        return publishCourseInfo;
    }


    //删除课程
    @Override
    public void removeCourse(String courseId) {
        //根据课程id删除小节
        eduVideoService.removeVideoByCourseId(courseId);

        //删除章节
        eduChapterService.removeChapterByCourseId(courseId);


        //删除描述
        courseDescriptionService.removeById(courseId);
        
        //删除课程本身
        int result = baseMapper.deleteById(courseId);
        if (result == 0 ){
            throw new GuliException(20001,"删除失败");
        }


    }

    //查询前八条热门课程
    @Cacheable(value = "course",key = "'selectCousrseList'")
    @Override
    public List<EduCourse> selctList() {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        wrapper.last("limit 8");
        List<EduCourse> eduList =baseMapper.selectList(wrapper);
        return eduList;
    }

    //条件查询带分页查询数据
    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> eduCoursePage,CourseFronVo courseFronVo) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        //判断条件值是否温控
        if (!StringUtils.isEmpty(courseFronVo.getSubjectParentId())) {
            //一级分类
            wrapper.eq( "subject_parent_id",courseFronVo.getSubjectParentId());
        }
        //二级分类
        if (!StringUtils.isEmpty(courseFronVo.getSubjectId())) {
            wrapper.eq( "subject_id",courseFronVo.getSubjectId());
        }

        //关注度
        if (!StringUtils.isEmpty(courseFronVo.getBuyCountSort())) {
            wrapper.orderByDesc("buy_count");
        }
        //最新
        if (!StringUtils.isEmpty(courseFronVo.getGmtCreateSort())) {

            wrapper.orderByDesc("gmt_create");

        }
        //价格
        if (!StringUtils.isEmpty(courseFronVo.getPriceSort())) {
            wrapper.orderByDesc("price");
        }

        baseMapper.selectPage(eduCoursePage,wrapper);


        List<EduCourse> records = eduCoursePage.getRecords();
        long current = eduCoursePage.getCurrent();
        long pages = eduCoursePage.getPages();
        long size = eduCoursePage.getSize();
        long total = eduCoursePage.getTotal();
        boolean hasNext = eduCoursePage.hasNext();
        boolean hasPrevious = eduCoursePage.hasPrevious();
        //把分页数据获取出来 放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        return map;
    }

    //根据课程id
    @Override
    public CourseWebVo getBaseCouseInfo(String coursId) {

        return baseMapper.getBaseCoureInfo(coursId);
    }

}
