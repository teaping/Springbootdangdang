package com.atguigu.eduserice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.atguigu.eduserice.entity.EduSubject;
import com.atguigu.eduserice.entity.excel.SubjectData;
import com.atguigu.eduserice.entity.subject.OneSubject;
import com.atguigu.eduserice.entity.subject.TwoSubject;
import com.atguigu.eduserice.listener.SubjectExcelListener;
import com.atguigu.eduserice.mapper.EduSubjectMapper;
import com.atguigu.eduserice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-03-31
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {


    //添加课程分类
    @Override
    public void saveSubject(MultipartFile file,EduSubjectService subjectService) {
        try {
            //文件输入流
            InputStream in = file.getInputStream();
            //调用方法读取
            EasyExcel.read(in,SubjectData.class,new SubjectExcelListener(subjectService)).excelType(ExcelTypeEnum.XLSX).sheet().doRead();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //课程分类列表功能(树形)
    @Override
    public List<OneSubject> getAllOnTwoSubject() {
        //查询所有分类
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id","0");
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);

        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        wrapperTwo.ne("parent_id","0");
        List<EduSubject> TwoSubjectList = baseMapper.selectList(wrapperTwo);
        //封装
        List<OneSubject> fianlSubjectList = new ArrayList<>();

        for (int i = 0; i < oneSubjectList.size(); i++) {
            EduSubject eduSubject = oneSubjectList.get(i);

            OneSubject oneSubject=new OneSubject();
//            oneSubject.setId(eduSubject.getId());
//            oneSubject.setTitle(eduSubject.getTitle());

            BeanUtils.copyProperties(eduSubject,oneSubject);

            fianlSubjectList.add(oneSubject);

            //二级分类要在一级分类中做封装
            List<TwoSubject> twoFanalSubjectList = new ArrayList<>();

            for (int m = 0; m < TwoSubjectList.size(); m++) {
                EduSubject tSubject = TwoSubjectList.get(m);
                if (tSubject.getParentId().equals(eduSubject.getId())){
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(tSubject,twoSubject);
                    twoFanalSubjectList.add(twoSubject);
                }
            }
            oneSubject.setChildren(twoFanalSubjectList);

        }
        return fianlSubjectList;
    }
}
