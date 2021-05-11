package com.atguigu.eduserice.service;

import com.atguigu.eduserice.entity.EduChapter;
import com.atguigu.eduserice.entity.chapter.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-04-01
 */
public interface EduChapterService extends IService<EduChapter> {

    //课程大纲列表 根据课程id查询
    List<ChapterVo> getChapterVideoByCoursId(String courseId);

    //删除章节
    boolean deleteChapter(String chapterId);


    //删除章节
    void removeChapterByCourseId(String courseId);
}
