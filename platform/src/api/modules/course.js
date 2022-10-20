import { getRequest, postRequest, postJsonRequest, deleteRequest } from '@/api/axios'

export const coursebyinterest = (params) => {
    return postRequest('sys/course/getCourseByInterest', params)
}

//通过课程id查询课程内容
export const getCourseById = (params) => {
    return postRequest('sys/course/getCourseById', params)
}
//通过课程名查询课程
export const getCourseByName = (params) => {
    return postRequest('sys/course/getCourseByName', params)
}
//通过课程查询对应子类别
export const getpcategorybycourse = (params) => {
    return postRequest('sys/child-category/getChildCategoryByCourseId', params)
}
//通过子类别查询课程
export const getCourseByCategory = (params) => {
    return postRequest('sys/course/getCourseByCategory', params)
}
//通过父类别查询所有所有课程
export const getCourseByParentCategoryId = (params) => {
    return postRequest('sys/course/getCourseByParentCategoryId', params)
} 

export const getMyCourseList = (params) => {
    return postRequest('sys/course/getMyCourseList', params)
}   

export const getCommentsById = (params) => {
    return postRequest('sys/course-comment/getCommentsById', params)
} 



export const comment = (params) => {
    return postRequest('sys/student/comment', params)
} 
