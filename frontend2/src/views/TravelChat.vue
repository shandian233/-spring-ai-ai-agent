<script setup lang="ts">
import { ref, nextTick, watch } from 'vue'
import { Button } from '@/components/ui/button'
import { ScrollArea } from '@/components/ui/scroll-area'
import MessageBubble from '@/components/chat/MessageBubble.vue'
import MessageInput from '@/components/chat/MessageInput.vue'
import TypingIndicator from '@/components/chat/TypingIndicator.vue'
import ConnectionStatus from '@/components/chat/ConnectionStatus.vue'
import { useChat } from '@/composables/useChat'

const ENDPOINT = '/ai/travel_app/chat/sse'

const { messages, connectionStatus, isStreaming, sendMessage, clearMessages } = useChat(ENDPOINT, true)

const messagesContainer = ref<HTMLElement | null>(null)

function scrollToBottom() {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

watch(
  () => messages.value.length,
  () => scrollToBottom()
)

watch(
  () => isStreaming.value,
  (streaming) => {
    if (!streaming) scrollToBottom()
  }
)

function handleSend(message: string) {
  sendMessage(message)
}

const suggestions = [
  '推荐南京美食',
  '北京三日游攻略',
  '上海周边古镇',
  '适合情侣的旅行地',
]
</script>

<template>
  <div class="flex h-[calc(100dvh-3.5rem)] flex-col">
    <div class="border-b bg-muted/30">
      <div class="container mx-auto max-w-[1200px] px-4 py-3">
        <div class="flex items-center justify-between">
          <div>
            <h2 class="text-lg font-semibold">旅行助手</h2>
            <p class="text-sm text-muted-foreground">智能规划您的旅行</p>
          </div>
          <div class="flex items-center gap-4">
            <ConnectionStatus :status="connectionStatus" />
            <Button variant="ghost" size="sm" @click="clearMessages">
              清空对话
            </Button>
          </div>
        </div>
      </div>
    </div>

    <ScrollArea class="flex-1">
      <div ref="messagesContainer" class="container mx-auto max-w-[800px] px-4 py-6">
        <div v-if="messages.length === 0" class="flex flex-col items-center justify-center py-12 text-center">
          <div class="mb-4 rounded-full bg-primary/10 p-4">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-primary" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5"/>
            </svg>
          </div>
          <h3 class="mb-2 text-lg font-semibold">开始您的旅行规划</h3>
          <p class="mb-6 text-sm text-muted-foreground">输入您想去的城市或景点，AI将为您推荐最佳行程</p>
          <div class="flex flex-wrap justify-center gap-2">
            <Button
              v-for="suggestion in suggestions"
              :key="suggestion"
              variant="outline"
              size="sm"
              class="text-muted-foreground"
              :disabled="isStreaming"
              @click="handleSend(suggestion)"
            >
              {{ suggestion }}
            </Button>
          </div>
        </div>

        <div v-else class="space-y-6">
          <MessageBubble
            v-for="message in messages"
            :key="message.id"
            :message="message"
          />
          <div v-if="isStreaming" class="flex gap-4">
            <div class="flex h-8 w-8 items-center justify-center rounded-full bg-muted">
              <span class="text-xs font-medium">AI</span>
            </div>
            <div class="rounded-2xl bg-muted px-4 py-2">
              <TypingIndicator />
            </div>
          </div>
        </div>
      </div>
    </ScrollArea>

    <div class="border-t bg-background">
      <div class="container mx-auto max-w-[800px] px-4 py-4">
        <MessageInput
          :disabled="isStreaming"
          :loading="connectionStatus === 'connecting'"
          @send="handleSend"
        />
      </div>
    </div>
  </div>
</template>
