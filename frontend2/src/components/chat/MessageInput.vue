<script setup lang="ts">
import { ref } from 'vue'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'

const props = defineProps<{
  disabled?: boolean
  loading?: boolean
}>()

const emit = defineEmits<{
  send: [message: string]
}>()

const message = ref('')

function handleSend() {
  if (!message.value.trim() || props.disabled) return
  emit('send', message.value)
  message.value = ''
}

function handleKeydown(event: KeyboardEvent) {
  if (event.key === 'Enter' && !event.shiftKey) {
    event.preventDefault()
    handleSend()
  }
}
</script>

<template>
  <div class="flex w-full items-end gap-2">
    <Input
      v-model="message"
      type="text"
      placeholder="输入消息..."
      :disabled="disabled"
      class="flex-1"
      @keydown="handleKeydown"
    />
    <Button
      size="icon"
      :disabled="disabled || !message.trim()"
      @click="handleSend"
    >
      <svg
        v-if="!loading"
        xmlns="http://www.w3.org/2000/svg"
        width="20"
        height="20"
        viewBox="0 0 24 24"
        fill="none"
        stroke="currentColor"
        stroke-width="2"
        stroke-linecap="round"
        stroke-linejoin="round"
      >
        <path d="M22 2L11 13" />
        <path d="M22 2L15 22L11 13L2 9L22 2Z" />
      </svg>
      <svg
        v-else
        class="h-4 w-4 animate-spin"
        xmlns="http://www.w3.org/2000/svg"
        width="24"
        height="24"
        viewBox="0 0 24 24"
        fill="none"
        stroke="currentColor"
        stroke-width="2"
        stroke-linecap="round"
        stroke-linejoin="round"
      >
        <path d="M21 12a9 9 0 1 1-6.219-8.56" />
      </svg>
    </Button>
  </div>
</template>
