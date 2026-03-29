<script setup lang="ts">
import { RouterView } from 'vue-router'
import AppHeader from './AppHeader.vue'
</script>

<template>
  <div class="relative flex min-h-[100dvh] flex-col bg-background">
    <AppHeader />
    <main class="flex-1">
      <RouterView />
    </main>
    <footer class="border-t py-6 md:py-0">
      <div class="container mx-auto flex max-w-[1200px] flex-col items-center justify-between gap-4 md:h-24 md:flex-row px-4">
        <p class="text-center text-sm leading-loose text-muted-foreground md:text-left">
          &copy; 2026 Travel AI. All rights reserved.
        </p>
        <div class="flex items-center gap-4 text-sm text-muted-foreground">
          <a href="#" class="hover:text-foreground transition-colors">使用条款</a>
          <a href="#" class="hover:text-foreground transition-colors">隐私政策</a>
        </div>
      </div>
    </footer>
  </div>
</template>
