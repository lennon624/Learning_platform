import { getRequest, postRequest, postJsonRequest, deleteRequest } from '@/api/axios'

//获得消息列表
export const getMessage = (params) => {
    return postRequest('/sys/message/getMessage', params)
}

//消息是否已读切换
export const isViewTransfer = (params) => {
    return postRequest('/sys/message/isViewTransfer', params)
}

// //删除讨论（可能是子讨论也可能是根级讨论 需要区分
// export const deleteTalking = (params) => {
//     return postRequest('/sys/talkings/deleteTalking', params)
// }

