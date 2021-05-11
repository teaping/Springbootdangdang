package com.atguigu.eduserice.controller.front;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.ordervo.CourseWebVoOrder;
import com.atguigu.eduserice.client.OrdersClint;
import com.atguigu.eduserice.entity.EduCourse;
import com.atguigu.eduserice.entity.EduTeacher;
import com.atguigu.eduserice.entity.chapter.ChapterVo;
import com.atguigu.eduserice.entity.frontvo.CourseFronVo;
import com.atguigu.eduserice.entity.frontvo.CourseWebVo;
import com.atguigu.eduserice.service.EduChapterService;
import com.atguigu.eduserice.service.EduCourseService;
import com.atguigu.eduserice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/coursefront")
@CrossOrigin
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrdersClint ordersClint;


    //条件查询带分页查询数据
    @PostMapping("getFrontCourseList/{page}/{limit}")
    public R getFrontCourseList(@PathVariable long page, @PathVariable long limit,
                                @RequestBody(required = false) CourseFronVo courseFronVo) {
        Page<EduCourse> eduCoursePage = new Page<>(page,limit);
        Map<String, Object> map = courseService.getCourseFrontList(eduCoursePage,courseFronVo);
        return R.ok().data(map);
    }

    //课程详情
    @GetMapping("getFrontCourseInfo/{coursId}")
    public R getFrontCourseInfo(@PathVariable String coursId, HttpServletRequest request) {
        //根据课程id
        CourseWebVo courseWebVo = courseService.getBaseCouseInfo(coursId);

        //根据课程id查询章节和小节
        List<ChapterVo> chapterVideoByCoursId = chapterService.getChapterVideoByCoursId(coursId);


        //根据课程id查询当前课程id是否已支付
        boolean byCourse = ordersClint.isByCourse(coursId, JwtUtils.getMemberIdByJwtToken(request));


        return R.ok().data("courseWebVo",courseWebVo).data("chapterVideoByCoursId",chapterVideoByCoursId).data("isBuy",byCourse);

    }

    //根据课程id查询课程信息
    @PostMapping("getCouseInfoOrder/{id}")
    public CourseWebVoOrder getCouseInfoOrder(@PathVariable String id) {
        CourseWebVo baseCouseInfo = courseService.getBaseCouseInfo(id);
        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();
        BeanUtils.copyProperties(baseCouseInfo, courseWebVoOrder);
        return courseWebVoOrder;
    }

}
