import { getRequest, postRequest, postJsonRequest, deleteRequest } from '@/api/axios'

//发表讨论
export const reportTalking = (params) => {
    return postRequest('/sys/talkings/reportTalking', params)
}

//获得单个/全部题目讨论列表（单个或者全部由传到后端的参数决定 传空值-全部 传questionId-单个
export const getTalkings = (params) => {
    return postRequest('/sys/talkings/getTalkings', params)
}

//回复讨论
export const replyTalking = (params) => {
    return postRequest('/sys/talkings/replyTalking', params)
}

//删除讨论（可能是子讨论也可能是根级讨论 需要区分
export const deleteTalking = (params) => {
    return postRequest('/sys/talkings/deleteTalking', params)
}

