import request from '@/utils/request'

export default {
  // 条件查询分页
  getCourseList(page, limit, searchobj) {
    return request({
      url: `/eduservice/coursefront/getFrontCourseList/${page}/${limit}`,
      method: 'post',
      data: searchobj
    })
  },
  // 查询所有分类
  getAllSubject() {
    return request({
      url: `/eduservice/subject/getAllSubject`,
      method: 'get'
    })
  },
  // 课程详情查询
  getCourseInfo(id) {
    return request({
      url: `/eduservice/coursefront/getFrontCourseInfo/${id}`,
      method: 'get'
    })
  }
}

