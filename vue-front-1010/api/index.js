import request from '@/utils/request'
export default {

  // 查询热门课程和教授
  getIndexDatar() {
    return request({
      url: `/eduservice/indexfront/index`,
      method: 'get'
    })
  }

}
