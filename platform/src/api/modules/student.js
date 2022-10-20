import { getRequest,postRequest,postJsonRequest,deleteRequest,postUploadRequest} from '@/api/axios'

export const queryMyList = (params) => {
    return postRequest('sys/student/queryMyList',params)
}
export const addCourse2List = (params) => {
    return postRequest('sys/student/addCourse2List',params)
} 
export const removeCourseFromList = (params) => {
    return postRequest('sys/student/removeCourseFromList',params)
} 
export const selectInterests = (params) => {
    return postRequest('sys/student/selectInterests',params)
} 

export const upload = (params) => {
    return postUploadRequest('sys/student/doHomework', params)
}

export const addInterest = (params) => {
    return postRequest('sys/student/addInterest', params)
}

export const removeInterest = (params) => {
    return postRequest('sys/student/removeInterest', params)
}