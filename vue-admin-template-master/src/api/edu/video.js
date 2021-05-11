import request from '@/utils/request'
export default {
    //添加小节
    addVideo(Video) {
        return request({
            url: `/eduservice/video/addVideo`,
            method: 'post',
            data: Video
          })
    },
    //根据id查询章节
    getVideo(videoId) {
        return request({
            url: `/eduservice/video/getVideoInfo/${videoId}`,
            method: 'get'
          })
    },
    //修改章节
    updateVideo(Video) {
        return request({
            url: `/eduservice/video/updateVideo`,
            method: 'post',
            data: Video
          })
    },
    //删除章节
    daleteVideo(videoId) {
        return request({
            url: `/eduservice/video/${videoId}`,
            method: 'delete'
          })
    },
    deleteAliyunvod(id) {
        return request({
            url: `/eduvod/video/removeAlyVideo/${id}`,
            method: 'delete'
        })
    }

     
}