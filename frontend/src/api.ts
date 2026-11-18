import axios from 'axios'
import { ElMessage } from 'element-plus'
import type { ApiResponse } from './types'

export const api = axios.create({ baseURL: '/api', timeout: 10000 })

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('lost-found-token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const message = error.response?.data?.message || '网络连接失败，请稍后重试'
    ElMessage.error(message)
    if (error.response?.status === 401) {
      localStorage.removeItem('lost-found-token')
      localStorage.removeItem('lost-found-user')
    }
    return Promise.reject(error)
  },
)

export async function getData<T>(promise: Promise<{ data: ApiResponse<T> }>): Promise<T> {
  const response = await promise
  return response.data.data
}
