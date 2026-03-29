<script setup lang="ts">
</script>

<template>
  <div class="flex items-center gap-1 px-4 py-2">
    <span
      v-for="i in 3"
      :key="i"
      class="h-2 w-2 animate-bounce rounded-full bg-muted-foreground"
      :style="{ animationDelay: `${(i - 1) * 0.15}s` }"
    />
  </div>
</template>
