package com.atguigu.eduserice.controller.front;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.ordervo.CourseWebVoOrder;
import com.atguigu.eduserice.client.CommClient;
import com.atguigu.eduserice.entity.EduComment;
import com.atguigu.eduserice.service.EduCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/comment")
@CrossOrigin
public class CommentFrontController {
    @Autowired
    private EduCommentService commentService;

    @Autowired
    private CommClient commClient;

    //根据课程id查询评论列表

    @ApiOperation(value = "评论分页列表")

    @GetMapping("{page}/{limit}")
    public R index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(name = "courseQuery", value = "查询对象", required = false) String courseId) {
        Page<EduComment> pageParam = new Page<>(page, limit);
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        commentService.page(pageParam,wrapper);
        List<EduComment> commentList = pageParam.getRecords();
        Map<String, Object> map = new HashMap<>();
        map.put("items", commentList);
        map.put("current", pageParam.getCurrent());
        map.put("pages", pageParam.getPages());
        map.put("size", pageParam.getSize());
        map.put("total", pageParam.getTotal());
        map.put("hasNext", pageParam.hasNext());
        map.put("hasPrevious", pageParam.hasPrevious());
        return R.ok().data(map);

    }



    @ApiOperation(value = "添加评论")

    @PostMapping("auth/save")

    public R save(@RequestBody EduComment comment, HttpServletRequest request) {

        String memberId = JwtUtils.getMemberIdByJwtToken(request);

        if(StringUtils.isEmpty(memberId)) {

            return R.err().code(28004).message("请登录");

        }

        comment.setMemberId(memberId);


        R pay = commClient.getUcenterPay(memberId);
        Map<String, Object> data = pay.getData();
        List<String> lista = new ArrayList<>();
        for(Map.Entry<String, Object> vo : data.entrySet()){
            vo.getKey();
             lista = (List<String>) vo.getValue();
        }



        comment.setNickname(lista.get(1));

        comment.setAvatar(lista.get(2));


        commentService.save(comment);

        return R.ok();

    }



}
