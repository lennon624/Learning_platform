import { getRequest, postRequest, postJsonRequest, deleteRequest } from '@/api/axios'

export const login = (params) => {
    return postRequest('sys/user/login', params)
}

export const register = (params) => {
    return postJsonRequest('sys/user/register',params)
}

export const sendCode = (params) => {
    return postRequest('sys/user/sendCode', params)
}

export const refineInfo = (params) => {
    return postRequest('sys/user/refineInfo', params)
}

//个人信息的设置（邮箱和手机号）
export const editSetting = (params) => {
    return postRequest('sys/user/editSetting', params)
}

export const loginByTel = (params) => { 
    return postRequest('sys/user/loginByTel', params)
}

export const sendTelCode = (params) => {
    return postRequest('sys/user/sendTelCode', params)

}

export const logout1 = (params) => {
    return getRequest('sys/user/logout', params)
}

export const page = (params) => {
    return postRequest('sys/user/page', params)
}

export const Upage = (params) => {
    return postRequest('sys/user/Upage', params)
}

export const add = (params) => {
    return postJsonRequest('sys/user/save', params)
}

export const updateUseful = (params) => {
    return postRequest('sys/user/updateUseful', params)
}

export const remove = (params) => {
    return deleteRequest('sys/user/delete', params)
}

export const checkEmailCode = (params) => {
    return postRequest('sys/user/checkEmailCode', params)
}

export const resetPassword = (params) => {
    return postRequest('sys/user/resetPassword', params)
}

export const sendEmailCode = (params) => {
    return postRequest('sys/user/sendEmailCode', params)
}