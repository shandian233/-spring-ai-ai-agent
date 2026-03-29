import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('@/views/HomePage.vue'),
    },
    {
      path: '/travel',
      name: 'travel',
      component: () => import('@/views/TravelChat.vue'),
    },
    {
      path: '/agent',
      name: 'agent',
      component: () => import('@/views/AgentChat.vue'),
    },
  ],
})

export default router
