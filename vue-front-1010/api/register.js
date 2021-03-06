import request from '@/utils/request'

export default {
  // 根据手机号码发送短信
  sendCode(mobile) {
    return request({
      url: `/edumsm/msm/send`,
      method: 'post',
      data: mobile
    })
  },
  // 用户注册
  registerMember(formItem) {
    return request({
      url: `/educenter/member/register`,
      method: 'post',
      data: formItem
    })
  }
}
