import { getRequest,postRequest,postJsonRequest,deleteRequest} from '@/api/axios'

export const ChapterByCourseId = (params) => {
    return postRequest('sys/chapter/getChapterByCourseId',params)
}

