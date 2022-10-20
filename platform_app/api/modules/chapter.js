import { getRequest,postRequest,postJsonRequest,deleteRequest} from '@/api/axios'

export const getChapterByCourseId = (params) => {
    return postRequest('sys/chapter/getChapterByCourseId',params)
}

