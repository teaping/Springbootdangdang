package com.atguigu.eduserice.service.impl;

import com.atguigu.eduserice.entity.EduChapter;
import com.atguigu.eduserice.entity.EduVideo;
import com.atguigu.eduserice.entity.chapter.ChapterVo;
import com.atguigu.eduserice.entity.chapter.VideoVo;
import com.atguigu.eduserice.mapper.EduChapterMapper;
import com.atguigu.eduserice.service.EduChapterService;
import com.atguigu.eduserice.service.EduVideoService;
import com.atguigu.sericebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-04-01
 */
@Service
public class  EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService videoService; //注入小节的service


    //课程大纲列表 根据课程id查询
    @Override
    public List<ChapterVo> getChapterVideoByCoursId(String courseId) {

        //跟着课程id 查询章节
        QueryWrapper<EduChapter> wrapperChaper = new QueryWrapper<>();
        wrapperChaper.eq("course_id",courseId);
        List<EduChapter> eduChapList = baseMapper.selectList(wrapperChaper);
        //跟着课程id查询小节
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id",courseId);
        List<EduVideo> eduVideoList = videoService.list(wrapperVideo);

        //遍历章节lsit封装
        List<ChapterVo> finalList = new ArrayList<>();

        for (int i = 0; i < eduChapList.size(); i++) {
            //每个章节
            EduChapter eduChapter = eduChapList.get(i);
            //把eduChapter对象中的值赋值到ChapterVo
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);

            finalList.add(chapterVo);

            List<VideoVo> videoVoList =new ArrayList<>();
            //遍历小节
            for (int m = 0; m < eduVideoList.size(); m++) {
                EduVideo eduVideo = eduVideoList.get(m);
                //判断 小节id 和章节id
                if(eduVideo.getChapterId().equals(eduChapter.getId())) {
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    videoVoList.add(videoVo);
                }
            }
            //把封装之后的小节list集合 放到章节中
            chapterVo.setChildren(videoVoList);
        }
        return finalList;
    }



    //删除章节
    @Override
    public boolean deleteChapter(String chapterId) {
        //查询小节表 如果查询数据 不删除
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);
        int count = videoService.count(wrapper);
        //判断
        if (count >0){ //查询出小节 不进行删除
            throw new GuliException(20001,"不能删除");

        } else {        //查不出来，进行删除
            //删除章节
            int result = baseMapper.deleteById(chapterId);
            return result>0;
        }


    }


    //删除章节
    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper =new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
