<script setup lang="ts">
import { RouterLink, useRoute } from 'vue-router'
import { Button } from '@/components/ui/button'

const route = useRoute()

const navLinks = [
  { path: '/', label: '首页', name: 'home' },
  { path: '/travel', label: '旅行助手', name: 'travel' },
  { path: '/agent', label: '超级Agent', name: 'agent' },
]
</script>

<template>
  <header class="sticky top-0 z-50 w-full border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
    <div class="container mx-auto flex h-14 max-w-[1200px] items-center px-4">
      <div class="mr-4 flex">
        <RouterLink to="/" class="mr-6 flex items-center space-x-2">
          <span class="text-xl font-bold tracking-tight">Travel AI</span>
        </RouterLink>
        <nav class="flex items-center space-x-6 text-sm font-medium">
          <RouterLink
            v-for="link in navLinks"
            :key="link.path"
            :to="link.path"
            class="transition-colors hover:text-foreground/80"
            :class="route.name === link.name ? 'text-foreground' : 'text-foreground/60'"
          >
            {{ link.label }}
          </RouterLink>
        </nav>
      </div>
      <div class="ml-auto flex items-center space-x-2">
        <Button variant="ghost" size="sm">登录</Button>
        <Button size="sm">开始使用</Button>
      </div>
    </div>
  </header>
</template>
