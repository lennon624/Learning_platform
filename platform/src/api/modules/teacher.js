import { postUploadRequest } from '@/api/axios'

export const upload = (params) => {
    return postUploadRequest('sys/teacher/uploadFile', params)
}