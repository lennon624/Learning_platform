import axios from '@/js_sdk/gangdiedao-uni-axios'
import Qs from 'qs'

// 超时设定
axios.defaults.timeout = 15000
// 本地测试
// axios.defaults.baseURL = "http://localhost:8686/xk";
// 真机测试
axios.defaults.baseURL = "http://jp-tyo-dvm-2.sakurafrp.com:62759/xk";

//http request 拦截器
axios.interceptors.request.use(config => {
  return config
}, err => {
  Message({ message: '请求超时', type: 'error' })
  return Promise.reject(err)
})

// http response 拦截器
axios.interceptors.response.use(response => {

  const data = response.data;
  if (data.success) {//如果返回success
    return Promise.resolve(response)
  }

  // 根据返回的code值来做不同的处理(和后端约定)
  switch (data.statusCode) {
    case '401':
      console.log('401:' + data.message)
      return Promise.reject(data.message)
    default:
      console.log('未知错误：' + data.message)
      return Promise.reject(data.message)
  }

}, (err) => { //响应失败时的处理
  // 返回状态码不为200时候的错误处理
  return Promise.reject(err)
})

//封装 getRequest
export function getRequest(url,params) {

  return axios({
    method: 'get',
    url: `${url}`,
    params: params

  })
}

export function deleteRequest(url, params) {

  return axios({
    method: 'delete',
    url: `${url}?id=` + params,
    data: { id: params }

  })
}

export function postRequest(url, params) {
  return axios({
    method: 'post',
    url: `${url}`,
    data: params,
    transformRequest: [function (data) {
      return Qs.stringify(data, { allowDots: true })
    }],
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
    }
  })
}


//封装 postRequest-json数据格式
export function postJsonRequest(url, params) {
  return axios({
    method: 'post',
    url: `${url}`,
    data: params,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

//封装文件格式
export function postUploadRequest(url, formData) {
  return axios({
    method: 'post',
    url: `${url}`,
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}