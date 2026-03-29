import { ref, onUnmounted } from 'vue'
import { createSSEConnection, API_BASE_URL } from '@/api'
import { useStorage } from '@vueuse/core'

export interface Message {
  id: string
  content: string
  isUser: boolean
  timestamp: number
  status: 'sending' | 'sent' | 'error'
  type: 'text' | 'error' | 'system'
}

export type ConnectionStatus = 'disconnected' | 'connecting' | 'connected' | 'error'

export function useChat(endpoint: string, useChatId: boolean = true) {
  const messages = useStorage<Message[]>('chat-messages', [])
  const connectionStatus = ref<ConnectionStatus>('disconnected')
  const isStreaming = ref(false)
  const currentMessage = ref('')
  const chatId = ref(generateChatId())

  let eventSource: EventSource | null = null

  function generateChatId(): string {
    return `chat_${Date.now()}_${Math.random().toString(36).substring(2, 9)}`
  }

  function generateMessageId(): string {
    return `msg_${Date.now()}_${Math.random().toString(36).substring(2, 9)}`
  }

  function buildURL(message: string): string {
    const base = `${API_BASE_URL}${endpoint}`
    const params = new URLSearchParams({ message })
    if (useChatId) {
      params.append('chatId', chatId.value)
    }
    return `${base}?${params.toString()}`
  }

  function sendMessage(content: string) {
    if (!content.trim() || isStreaming.value) return

    const userMessage: Message = {
      id: generateMessageId(),
      content: content.trim(),
      isUser: true,
      timestamp: Date.now(),
      status: 'sent',
      type: 'text',
    }
    messages.value.push(userMessage)

    const aiMessageId = generateMessageId()
    const aiMessage: Message = {
      id: aiMessageId,
      content: '',
      isUser: false,
      timestamp: Date.now(),
      status: 'sending',
      type: 'text',
    }
    messages.value.push(aiMessage)

    isStreaming.value = true
    connectionStatus.value = 'connecting'
    currentMessage.value = ''

    const url = buildURL(content)

    eventSource = createSSEConnection(
      url,
      (data) => {
        connectionStatus.value = 'connected'
        currentMessage.value += data
        const idx = messages.value.findIndex((m) => m.id === aiMessageId)
        if (idx !== -1) {
          messages.value[idx].content = currentMessage.value
        }
      },
      (error) => {
        console.error('SSE Error:', error)
        const idx = messages.value.findIndex((m) => m.id === aiMessageId)
        if (idx !== -1 && !currentMessage.value) {
          connectionStatus.value = 'error'
          messages.value[idx].status = 'error'
          messages.value[idx].content = '连接失败，请检查网络'
          messages.value[idx].type = 'error'
        } else {
          connectionStatus.value = 'disconnected'
        }
        isStreaming.value = false
      },
      () => {
        connectionStatus.value = 'disconnected'
        isStreaming.value = false
        const idx = messages.value.findIndex((m) => m.id === aiMessageId)
        if (idx !== -1) {
          messages.value[idx].status = 'sent'
        }
      }
    )
  }

  function clearMessages() {
    messages.value = []
    chatId.value = generateChatId()
    currentMessage.value = ''
  }

  function disconnect() {
    if (eventSource) {
      eventSource.close()
      eventSource = null
    }
    connectionStatus.value = 'disconnected'
    isStreaming.value = false
  }

  onUnmounted(() => {
    disconnect()
  })

  return {
    messages,
    connectionStatus,
    isStreaming,
    sendMessage,
    clearMessages,
    disconnect,
  }
}
