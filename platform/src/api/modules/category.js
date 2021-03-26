import { getRequest,postRequest,postJsonRequest,deleteRequest} from '@/api/axios'
//查询子类别
export const getchildcategory = (params) => {
    return postRequest('sys/child-category/getChildCategoryByParentId',params)
}
//获取所有子类别
export const getChildCategory2 = (params) => {
    return postRequest('sys/child-category/getChildCategory',params)
}
//查询所有父类别
export const getparentcategory = (params) => {
    return postRequest('sys/parent-category/getAllParentCategory',params)
}
//通过id查询父类别信息
export const getParentCategoryById = (params) => {
    return postRequest('sys/parent-category/getParentCategoryById',params)
}
//查询所有父类别信息
export const getAllParentCategory = (params) => {
    return postRequest('sys/parent-category/getAllParentCategory',params)
}  