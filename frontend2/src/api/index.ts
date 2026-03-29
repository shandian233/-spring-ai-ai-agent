import axios from 'axios'

const API_BASE_URL = import.meta.env.PROD
  ? '/api'
  : 'http://localhost:8123/api'

const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 60000,
  headers: {
    'Content-Type': 'application/json',
  },
})

export function createSSEConnection(
  url: string,
  onMessage: (data: string) => void,
  onError: (error: Event) => void,
  onComplete: () => void
) {
  let closedNormally = false
  const eventSource = new EventSource(url)

  eventSource.onmessage = (event) => {
    if (event.data === '[DONE]') {
      closedNormally = true
      eventSource.close()
      onComplete()
    } else {
      onMessage(event.data)
    }
  }

  eventSource.onerror = (error) => {
    if (!closedNormally) {
      onError(error)
    }
    eventSource.close()
  }

  return eventSource
}

export { API_BASE_URL }
export default api
