import { getRequest,postRequest,postJsonRequest,deleteRequest} from '@/api/axios'

export const getVideoSrcByChapterId = (params) => {
    return postRequest('sys/video/getVideoSrcByChapterId',params)
}

