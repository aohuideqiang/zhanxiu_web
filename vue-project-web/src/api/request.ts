import axios from 'axios'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000,
})

request.interceptors.response.use(
  (res) => {
    const data = res.data
    if (data && typeof data === 'object' && 'code' in data && 'data' in data) {
      return data.data
    }
    if (data && typeof data === 'object' && 'code' in data && 'rows' in data) {
      return data.rows
    }
    return data
  },
  (err) => {
    console.error('[API Error]', err?.message ?? err)
    return Promise.reject(err)
  },
)

export default request
