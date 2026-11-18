<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { api, getData } from '@/api'; import type { Item, Page } from '@/types'; import { categories } from '@/utils/labels'; import ItemCard from '@/components/ItemCard.vue'
const loading = ref(false); const pageData = ref<Page<Item>>({ content: [], totalElements: 0, totalPages: 0, number: 0, size: 12 })
const query = reactive({ keyword: '', type: '', category: '', page: 0 })
async function load() { loading.value = true; try { pageData.value = await getData(api.get('/items/public', { params: query })) } finally { loading.value = false } }
function search() { query.page = 0; load() }
onMounted(load)
</script>
<template>
  <div>
    <section class="hero"><div class="hero-content"><span class="eyebrow">CAMPUS LOST & FOUND</span><h1>遗失不慌张，<br><em>拾光</em>帮你找。</h1><p>连接每一位热心同学，让校园里的每一件物品，都能沿着善意回到主人身边。</p><div class="hero-search"><el-input v-model="query.keyword" size="large" placeholder="搜索物品名称、地点或描述" clearable @keyup.enter="search"/><el-button type="primary" size="large" @click="search">立即搜索</el-button></div><div class="hero-stats"><b>{{ pageData.totalElements }}</b><span>条有效信息正在被关注</span></div></div><div class="hero-visual"><div class="orbit orbit-one"></div><div class="orbit orbit-two"></div><div class="hero-card"><span>今日提示</span><b>描述越详细<br>找回越容易</b><small>请勿公开证件号码等敏感信息</small></div></div></section>
    <section class="content-section"><div class="section-heading"><div><span class="eyebrow">INFORMATION PLAZA</span><h2>信息广场</h2></div><p>最新发布的寻物与招领信息</p></div><div class="filter-bar"><el-segmented v-model="query.type" :options="[{ label: '全部', value: '' }, { label: '寻物启事', value: 'LOST' }, { label: '招领启事', value: 'FOUND' }]" @change="search"/><el-select v-model="query.category" placeholder="全部分类" clearable @change="search"><el-option v-for="item in categories" :key="item.value" v-bind="item"/></el-select></div>
      <div v-loading="loading" class="card-grid"><ItemCard v-for="item in pageData.content" :key="item.id" :item="item"/><el-empty v-if="!loading && !pageData.content.length" description="暂时没有符合条件的信息"/></div>
      <el-pagination v-if="pageData.totalPages > 1" background layout="prev, pager, next" :page-count="pageData.totalPages" :current-page="query.page + 1" @current-change="(p:number) => { query.page = p - 1; load() }"/>
    </section>
  </div>
</template>
