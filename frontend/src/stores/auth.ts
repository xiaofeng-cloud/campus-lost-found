import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import { api, getData } from '@/api'
import type { LoginResult, User } from '@/types'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('lost-found-token') || '')
  const saved = localStorage.getItem('lost-found-user')
  const user = ref<User | null>(saved ? JSON.parse(saved) : null)
  const loggedIn = computed(() => !!token.value && !!user.value)
  const isAdmin = computed(() => user.value?.role === 'ADMIN')

  async function login(username: string, password: string) {
    const result = await getData<LoginResult>(api.post('/auth/login', { username, password }))
    token.value = result.token; user.value = result.user
    localStorage.setItem('lost-found-token', result.token)
    localStorage.setItem('lost-found-user', JSON.stringify(result.user))
  }
  async function register(payload: { username: string; nickname: string; password: string; phone: string }) {
    await api.post('/auth/register', payload)
  }
  function logout() {
    token.value = ''; user.value = null
    localStorage.removeItem('lost-found-token'); localStorage.removeItem('lost-found-user')
  }
  return { token, user, loggedIn, isAdmin, login, register, logout }
})
