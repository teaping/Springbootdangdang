package com.atguigu.eduserice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduserice.entity.EduTeacher;
import com.atguigu.eduserice.entity.vo.TeacherQuery;
import com.atguigu.eduserice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-03-23
 */
//@Api(description = "讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin //解决跨域
public class EduTeacherController {

    @Autowired
    private EduTeacherService teacherService;

    //查询所有
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("findAll")
    public R findAllTeacher() {
        List<EduTeacher> list = teacherService.list(null);
        return  R.ok().data("items",list);
    }

    //删除讲师
    @ApiOperation(value = "根据id删除讲师")
    @DeleteMapping("{id}")
    public R removeTeacher(@ApiParam(name = "id", value = "讲师ID", required = true)@PathVariable String id){
        boolean flag = teacherService.removeById(id) ;
        if(flag){
            return R.ok();
        }else{
            return R.err();
        }
    }

    //分页查询讲师方法
    @ApiOperation(value = "分页查询讲师方法")
    @GetMapping("pageTeacher/{current}/{limlt}")
    public R pageListTeacher(@PathVariable long current, @PathVariable long limlt) {

        Page<EduTeacher> pageTeacher = new Page<>(current,limlt);

        teacherService.page(pageTeacher, null);

        long total = pageTeacher.getTotal();//总记录数
        List<EduTeacher> records = pageTeacher.getRecords();

        Map map=new HashMap();
        map.put("total",total);
        map.put("rows",records);
        return  R.ok().data(map);
    }


    //条件查询带分页
    @ApiOperation(value = "条件查询带分页")
    @PostMapping("pageTeacherCondition/{current}/{limlt}")
    public R pageTeacherCondition(@PathVariable long current,
                                  @PathVariable long limlt,
                                  @RequestBody(required = false) TeacherQuery teacherQuery){

        Page<EduTeacher> pageTeacher = new Page<>(current,limlt);

        //构建条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        //多条件组合查询 动态sql
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        if(!StringUtils.isEmpty(name)) {
            wrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(level)) {
            wrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(begin)) {
            wrapper.le("gmt_create",end);
        }


        //根据时间排序
        wrapper.orderByDesc("gmt_create");


        teacherService.page(pageTeacher, wrapper);
        long total = pageTeacher.getTotal();//总记录数
        List<EduTeacher> records = pageTeacher.getRecords();

        return  R.ok().data("total",total).data("rows", records);

    }

    //添加讲师方法
    @ApiOperation(value = "添加讲师方法")
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean save = teacherService.save(eduTeacher);
        if (save) {
            return R.ok();
        } else {
            return R.err();
        }
    }

    //讲师修改(根据讲师id查询 进行数据回显)
    @ApiOperation(value = "讲师修改(根据讲师id查询 进行数据回显)")
    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable String id){
        EduTeacher eduTeacher = teacherService.getById(id);
        return R.ok().data("teacher",eduTeacher);
    }

    @ApiOperation(value = "讲师修改")
    @PostMapping("updareTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean flag= teacherService.updateById(eduTeacher);
        return flag?R.ok():R.err();
    }



}

