<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { RouterLink } from 'vue-router'
import gsap from 'gsap'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'

const heroTitle = ref<HTMLElement | null>(null)
const heroSubtitle = ref<HTMLElement | null>(null)
const heroButtons = ref<HTMLElement | null>(null)
const cards = ref<HTMLElement[]>([])
const stats = ref<HTMLElement | null>(null)
const floatingShapes = ref<HTMLElement[]>([])

const features = [
  {
    title: '旅行助手',
    description: '智能规划您的旅行行程，推荐景点、美食和住宿',
    icon: 'M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5',
    link: '/travel',
    color: 'from-blue-500 to-cyan-500',
    glow: 'group-hover:shadow-blue-500/25',
  },
  {
    title: '超级Agent',
    description: '全能AI助手，满足您的各种需求',
    icon: 'M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z',
    link: '/agent',
    color: 'from-purple-500 to-pink-500',
    glow: 'group-hover:shadow-purple-500/25',
  },
]

onMounted(() => {
  const tl = gsap.timeline({ defaults: { ease: 'power3.out' } })

  tl.from(floatingShapes.value, {
    scale: 0,
    opacity: 0,
    rotation: -180,
    duration: 1.2,
    stagger: 0.15,
  })
    .from(heroTitle.value, {
      y: 60,
      opacity: 0,
      duration: 0.8,
    }, '-=0.6')
    .from(heroSubtitle.value, {
      y: 40,
      opacity: 0,
      duration: 0.6,
    }, '-=0.4')
    .from(heroButtons.value, {
      y: 30,
      opacity: 0,
      duration: 0.5,
    }, '-=0.3')
    .from(cards.value, {
      y: 80,
      opacity: 0,
      duration: 0.8,
      stagger: 0.15,
    }, '-=0.4')
    .from(stats.value?.children || [], {
      y: 30,
      opacity: 0,
      duration: 0.5,
      stagger: 0.1,
    }, '-=0.3')

  gsap.to('.floating-shape', {
    y: 'random(-20, 20)',
    x: 'random(-15, 15)',
    rotation: 'random(-10, 10)',
    duration: 'random(3, 5)',
    repeat: -1,
    yoyo: true,
    ease: 'sine.inOut',
    stagger: {
      each: 0.5,
      from: 'random',
    },
  })
})
</script>

<template>
  <div class="relative min-h-[calc(100dvh-3.5rem)] overflow-hidden bg-background">
    <div class="absolute inset-0 bg-[radial-gradient(ellipse_at_top,_var(--tw-gradient-stops))] from-indigo-900/30 via-background to-background" />
    
    <div class="absolute inset-0 overflow-hidden">
      <div
        v-for="i in 6"
        :key="i"
        ref="floatingShapes"
        class="floating-shape absolute rounded-full opacity-20 blur-3xl"
        :class="i <= 3 ? 'bg-indigo-500' : 'bg-purple-500'"
        :style="{
          width: i <= 3 ? `${200 + i * 80}px` : `${150 + i * 50}px`,
          height: i <= 3 ? `${200 + i * 80}px` : `${150 + i * 50}px`,
          top: `${10 + i * 12}%`,
          left: i % 2 === 0 ? `${60 + i * 5}%` : `${5 + i * 8}%`,
        }"
      />
    </div>

    <div class="absolute inset-0 bg-[linear-gradient(to_right,#80808008_1px,transparent_1px),linear-gradient(to_bottom,#80808008_1px,transparent_1px)] bg-[size:32px_32px] [mask-image:radial-gradient(ellipse_60%_60%_at_50%_50%,black_40%,transparent_100%)]" />
    
    <div class="container relative mx-auto max-w-[1200px] px-4 py-20 md:py-28">
      <div class="mx-auto max-w-3xl text-center">
        <div ref="heroTitle" class="mb-6">
          <h1 class="text-5xl font-bold tracking-tight md:text-6xl lg:text-7xl">
            Travel AI
            <span class="bg-gradient-to-r from-indigo-500 via-purple-500 to-pink-500 bg-clip-text text-transparent animate-gradient">
              智能旅行
            </span>
          </h1>
        </div>
        <p ref="heroSubtitle" class="mb-10 text-lg text-muted-foreground md:text-xl leading-relaxed">
          基于AI的旅行规划助手，让您的旅行更加智能、便捷<br />
          <span class="text-foreground/80">开启您的智能旅行新时代</span>
        </p>
        <div ref="heroButtons" class="flex flex-col sm:flex-row items-center justify-center gap-4">
          <RouterLink to="/travel">
            <Button size="lg" class="h-12 px-8 text-base relative overflow-hidden group">
              <span class="relative z-10 flex items-center">
                立即体验
                <svg xmlns="http://www.w3.org/2000/svg" class="ml-2 h-4 w-4 transition-transform group-hover:translate-x-1" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M5 12h14M12 5l7 7-7 7"/>
                </svg>
              </span>
              <div class="absolute inset-0 bg-gradient-to-r from-indigo-600 to-purple-600 opacity-0 transition-opacity group-hover:opacity-100" />
            </Button>
          </RouterLink>
          <RouterLink to="/agent">
            <Button size="lg" variant="outline" class="h-12 px-8 text-base">
              了解更多
            </Button>
          </RouterLink>
        </div>
      </div>

      <div ref="cards" class="mt-24 grid gap-6 md:grid-cols-2 lg:gap-8 max-w-4xl mx-auto">
        <RouterLink
          v-for="feature in features"
          :key="feature.title"
          :to="feature.link"
          class="group"
        >
          <Card class="h-full overflow-hidden border-border/50 bg-background/60 backdrop-blur-sm transition-all duration-500 hover:-translate-y-2 hover:shadow-xl hover:shadow-indigo-500/10" :class="feature.glow">
            <div class="absolute inset-0 bg-gradient-to-br opacity-0 transition-opacity duration-500 group-hover:opacity-5" :class="feature.color.replace('from-', 'bg-')" />
            <CardHeader class="relative">
              <div class="mb-4 inline-flex h-14 w-14 items-center justify-center rounded-2xl bg-gradient-to-br shadow-lg transition-transform duration-300 group-hover:scale-110 group-hover:rotate-3" :class="feature.color">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-7 w-7 text-white" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path :d="feature.icon" />
                </svg>
              </div>
              <CardTitle class="text-2xl font-semibold">{{ feature.title }}</CardTitle>
              <CardDescription class="text-base">{{ feature.description }}</CardDescription>
            </CardHeader>
            <CardContent class="relative">
              <div class="flex items-center font-medium text-primary transition-transform duration-300 group-hover:translate-x-2">
                开始使用
                <svg xmlns="http://www.w3.org/2000/svg" class="ml-2 h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M5 12h14M12 5l7 7-7 7"/>
                </svg>
              </div>
            </CardContent>
          </Card>
        </RouterLink>
      </div>

      <div ref="stats" class="mt-28 grid grid-cols-3 gap-8 max-w-2xl mx-auto">
        <div class="space-y-2 text-center">
          <div class="text-4xl font-bold md:text-5xl bg-gradient-to-r from-indigo-500 to-purple-500 bg-clip-text text-transparent">99.9%</div>
          <div class="text-sm text-muted-foreground">服务可用性</div>
        </div>
        <div class="space-y-2 text-center">
          <div class="text-4xl font-bold md:text-5xl bg-gradient-to-r from-purple-500 to-pink-500 bg-clip-text text-transparent">80+</div>
          <div class="text-sm text-muted-foreground">小红书严选：南京热门地点</div>
        </div>
        <div class="space-y-2 text-center">
          <div class="text-4xl font-bold md:text-5xl bg-gradient-to-r from-pink-500 to-rose-500 bg-clip-text text-transparent">24/7</div>
          <div class="text-sm text-muted-foreground">在线支持</div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
@keyframes gradient-shift {
  0%, 100% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
}

.animate-gradient {
  background-size: 200% 200%;
  animation: gradient-shift 4s ease infinite;
}
</style>
