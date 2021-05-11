import request from '@/utils/request'
export default {
    //1添加课程信息功能
    addCourseInfo(courseInfo) {
        return request({
            //url: '/eduservice/teacher/pageTeacherCondition/'+current+"/"+limit,
            url: `/eduservice/edu-course/addCourseInfo`,
            method: 'post',
            data:courseInfo
          })
    },
    //查询所有讲师
    getListTeacher() {
        return request({
            //url: '/eduservice/teacher/pageTeacherCondition/'+current+"/"+limit,
            url: `/eduservice/teacher/findAll`,
            method: 'get'
          })
    },
    //根据课程id查询课程基本信息
    getCourseInfoId(id) {
        return request({
            //url: '/eduservice/teacher/pageTeacherCondition/'+current+"/"+limit,
            url: `/eduservice/edu-course/getCourseInfo/`+id,
            method: 'get'
          })
    },
    //修改课程信息
    getUpdateInfo(courseInfo) {
        return request({
            //url: '/eduservice/teacher/pageTeacherCondition/'+current+"/"+limit,
            url: `/eduservice/edu-course/updateCourseInfo`,
            method: 'post',
            data:courseInfo
          })
    },
    //课程确认信息显示
    getPublishCourseInfo(id) {
        return request({
            url: `/eduservice/edu-course/getPublishCourseInfo/${id}`,
            method: 'get'
          })
    },

    //课程最终发布
    getpublishCourse(id) {
        return request({
            url: '/eduservice/edu-course/publishCourse/'+id,
            method: 'post'
          })
    },
    //课程列表
    getListCourse(current,limit,CourseQuery) {
        return request({
            url: `/eduservice/edu-course/pageCouruseCondition/${current}/${limit}`,
            method: 'post',
            data:CourseQuery
          })
    },
    deleteCoureId(Id) {
        return request({
            url: `/eduservice/edu-course/${Id}`,
            method: 'delete'
          })
    }


 }

