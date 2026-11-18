<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'
const auth = useAuthStore(); const router = useRouter()
function logout() { auth.logout(); router.push('/'); }
</script>

<template>
  <div class="app-shell">
    <header class="site-header">
      <router-link to="/" class="brand"><span class="brand-mark">拾</span><span><b>拾光校园</b><small>让每一件遗失都有归途</small></span></router-link>
      <nav>
        <router-link to="/">信息广场</router-link>
        <router-link v-if="auth.loggedIn" to="/publish">发布信息</router-link>
        <router-link v-if="auth.loggedIn" to="/center">个人中心</router-link>
        <router-link v-if="auth.isAdmin" to="/admin">管理后台</router-link>
      </nav>
      <div class="header-actions">
        <template v-if="auth.loggedIn"><span class="welcome">你好，{{ auth.user?.nickname }}</span><el-button text @click="logout">退出</el-button></template>
        <template v-else><router-link to="/login"><el-button>登录</el-button></router-link><router-link to="/register"><el-button type="primary">注册</el-button></router-link></template>
      </div>
    </header>
    <main><router-view /></main>
    <nav class="mobile-nav">
      <router-link to="/">广场</router-link>
      <router-link v-if="auth.loggedIn" to="/publish">发布</router-link>
      <router-link v-if="auth.loggedIn" to="/center">我的</router-link>
      <router-link v-if="auth.isAdmin" to="/admin">管理</router-link>
      <router-link v-if="!auth.loggedIn" to="/login">登录</router-link>
    </nav>
    <footer>© 2026 拾光校园 · 校园失物招领信息管理系统</footer>
  </div>
</template>
