import request from '@/utils/request'
export default {
    //生成统计数据
    createStaData(day) {
        return request({
            url: `/staservice/statisticsdaily/registerCount/${day}`,
            method: 'post'
          })
    },

    //获取统计数据显示
    getDataSta(searchObj) {
        return request({
            url: `/staservice/statisticsdaily/showData/${searchObj.type}/${searchObj.begin}/${searchObj.end}`,
            method: 'get'
          })
    }


     
}