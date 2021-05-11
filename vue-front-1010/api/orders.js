import request from '@/utils/request'
export default {
  // 生成订单接口
  createOrders(couseId) {
    return request({
      url: `/eduorder/order/createOrder/${couseId}`,
      method: 'post'
    })
  },
  // 根据订单id查询订单信息
  getOrdersInfo(id) {
    return request({
      url: `/eduorder/order/getOrderInfo/${id}`,
      method: 'get'
    })
  },
  // 生成二维码方法
  createNatvie(orderNo) {
    return request({
      url: `/eduorder/paylog/createNative/${orderNo}`,
      method: 'get'
    })
  },

  // 查询订单状态方法
  queryPayStatis(orderNo) {
    return request({
      url: `/eduorder/paylog/querPayStatus/${orderNo}`,
      method: 'get'
    })
  }

}
