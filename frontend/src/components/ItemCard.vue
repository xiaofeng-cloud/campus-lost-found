<script setup lang="ts">
import type { Item } from '@/types'; import { typeText, categoryText, statusText, formatTime } from '@/utils/labels'
defineProps<{ item: Item }>()
</script>
<template>
  <router-link :to="`/items/${item.id}`" class="item-card">
    <div class="card-image" :style="item.imageUrl ? { backgroundImage: `url(${item.imageUrl})` } : {}"><span v-if="!item.imageUrl">{{ item.postType === 'LOST' ? '寻' : '招' }}</span></div>
    <div class="card-body"><div class="tag-row"><el-tag :type="item.postType === 'LOST' ? 'danger' : 'success'" effect="light">{{ typeText[item.postType] }}</el-tag><el-tag type="info" effect="plain">{{ categoryText[item.category] }}</el-tag></div><h3>{{ item.title }}</h3><p>📍 {{ item.location }}</p><p>🕒 {{ formatTime(item.occurredAt) }}</p><div class="card-foot"><span>{{ item.publisherName }}</span><span class="status-dot">{{ statusText[item.status] }}</span></div></div>
  </router-link>
</template>
