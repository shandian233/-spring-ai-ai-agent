<script setup lang="ts">
import { computed } from 'vue'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import type { Message } from '@/composables/useChat'

const props = defineProps<{
  message: Message
}>()

const formattedTime = computed(() => {
  const date = new Date(props.message.timestamp)
  return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
})

const initials = computed(() => {
  return props.message.isUser ? 'U' : 'AI'
})
</script>

<template>
  <div
    class="flex w-full gap-4"
    :class="message.isUser ? 'flex-row-reverse' : 'flex-row'"
  >
    <Avatar class="h-8 w-8">
      <AvatarImage v-if="message.isUser" src="" />
      <AvatarFallback>{{ initials }}</AvatarFallback>
    </Avatar>
    <div
      class="relative max-w-[80%] rounded-2xl px-4 py-2"
      :class="[
        message.isUser
          ? 'bg-primary text-primary-foreground'
          : message.type === 'error'
            ? 'bg-destructive/10 text-destructive'
            : 'bg-muted'
      ]"
    >
      <div class="whitespace-pre-wrap break-words text-sm leading-relaxed">
        {{ message.content }}
        <span
          v-if="message.status === 'sending' && !message.isUser"
          class="ml-1 inline-block h-1.5 w-1.5 animate-pulse rounded-full bg-current"
        />
      </div>
      <span
        class="absolute bottom-1 text-[10px] opacity-60"
        :class="message.isUser ? 'right-3' : 'left-3'"
      >
        {{ formattedTime }}
      </span>
    </div>
  </div>
</template>
