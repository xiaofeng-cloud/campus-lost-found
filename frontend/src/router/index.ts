import { createRouter, createWebHistory } from 'vue-router'
import { decideAccess } from './access'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(),
  scrollBehavior: () => ({ top: 0 }),
  routes: [
    { path: '/', name: 'home', component: () => import('@/views/HomeView.vue') },
    { path: '/login', name: 'login', component: () => import('@/views/AuthView.vue'), props: { mode: 'login' } },
    { path: '/register', name: 'register', component: () => import('@/views/AuthView.vue'), props: { mode: 'register' } },
    { path: '/items/:id', name: 'detail', component: () => import('@/views/DetailView.vue') },
    { path: '/publish', name: 'publish', component: () => import('@/views/PublishView.vue'), meta: { requiresAuth: true } },
    { path: '/center', name: 'center', component: () => import('@/views/CenterView.vue'), meta: { requiresAuth: true } },
    { path: '/admin', name: 'admin', component: () => import('@/views/AdminView.vue'), meta: { requiresAuth: true, requiresAdmin: true } },
  ],
})

router.beforeEach((to) => decideAccess(to.meta, useAuthStore().user, to.fullPath))
export default router
