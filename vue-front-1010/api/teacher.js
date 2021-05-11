import request from '@/utils/request'

export default {
  // 分页讲师查询
  // 分页讲师查询的方法
  getTeacherList(page, limit) {
    return request({
      url: `/eduservice/teacherfront/getTeacherFronList/${page}/${limit}`,
      method: 'get'
    })
  },
  // 讲师详情的方法
  getTeacherInfo(id) {
    return request({
      url: `/eduservice/teacherfront/getTeacherFrontInfo/${id}`,
      method: 'get'
    })
  }
}
