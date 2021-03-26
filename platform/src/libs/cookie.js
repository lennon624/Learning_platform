import Cookie from 'js-cookie'

export const setCookie = (name = "default", value = '', cookieSetting = {}) => {
    let currentCookieSetting = {
       expires: 1 
    }
    Object.assign(cookieSetting, cookieSetting)
    Cookie.set(name,value,currentCookieSetting)
}
// 获取cookie
export const getCookie = (name = "default") => {
    return Cookie.get(name)
}
// 获取所有cookie
export const getAllCookie = () => {
    return Cookie.get()
}

//删除cookie
export const removeCookie = (name = "default") => {
    return Cookie.remove(name)
}

export default {
    setCookie,
    getCookie,
    getAllCookie,
    removeCookie
}