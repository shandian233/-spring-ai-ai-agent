<script setup lang="ts">
import type { ConnectionStatus } from '@/composables/useChat'

defineProps<{
  status: ConnectionStatus
}>()

const statusConfig = {
  disconnected: { label: '未连接', color: 'bg-muted' },
  connecting: { label: '连接中...', color: 'bg-yellow-500' },
  connected: { label: '已连接', color: 'bg-green-500' },
  error: { label: '连接错误', color: 'bg-red-500' },
}
</script>

<template>
  <div class="flex items-center gap-2 text-xs text-muted-foreground">
    <span
      class="h-2 w-2 rounded-full"
      :class="statusConfig[status].color"
    />
    <span>{{ statusConfig[status].label }}</span>
  </div>
</template>
