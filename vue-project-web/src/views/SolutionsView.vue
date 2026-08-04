<template>
  <div class="solutions-page">
    <PageBanner
      :image="banner.image"
      :title="banner.title"
      :desc="banner.desc"
      :badge="banner.badge"
      :actions="bannerActions"
      height="55vh"
    />

    <nav class="sub-nav">
      <div class="nav-container">
        <span
          v-for="tab in tabs"
          :key="tab.id"
          :class="['tab-item', { active: activeTab === tab.id }]"
          @click="activeTab = tab.id"
        >
          {{ tab.name }}
        </span>
      </div>
    </nav>

    <main class="content-area">
      <template v-for="t in tabs" :key="t.id">
        <section v-if="activeTab === t.id" class="solution-section">
          <h2>{{ t.name }}</h2>
          <p>{{ t.description }}</p>
        </section>
      </template>
    </main>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import PageBanner from './PageBanner.vue'
import { api } from '../api'
import type { BannerData, SolutionSubTab } from '../api'

const banner = ref<BannerData>({
  image:
    'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=intelligent+driving+data+platform+dashboard%2C+dark+blue+tech+background+with+subtle+grid+lines+and+data+visualization+elements%2C+futuristic+style%2C+high+contrast&image_size=landscape_16_9',
  title: '智能驾驶数据解决方案',
  desc: '搭建真实场景实现数据定制化，多类型标注保证数据质量',
  badge: '解决方案 · 智能驾驶',
})

const tabs = ref<(SolutionSubTab & { description: string })[]>([
  {
    id: 'driving',
    name: '智能驾驶',
    description: '面向自动驾驶、车道线、3D 点云场景，提供采集、标注、质检一体化方案。',
  },
  {
    id: 'cabin',
    name: '智能座舱',
    description: '提供座舱内的多模态交互数据服务，支持驾驶员行为、手势、语音等标注。',
  },
  {
    id: 'security',
    name: '智能安防',
    description: '视觉监控与异常行为检测解决方案，覆盖周界安防、人脸识别、行为分析。',
  },
])

const activeTab = ref('driving')

const bannerActions = [{ text: '获取方案', onClick: () => {} }]

async function loadData() {
  try {
    const [bannerData, tabsData] = await Promise.all([
      api.getSolutionBanner(),
      api.getSolutionSubTabs(),
    ])
    if (bannerData) banner.value = bannerData
    if (tabsData?.length) {
      tabs.value = tabsData.map((t, i) => ({
        ...t,
        description: tabs.value[i]?.description ?? '',
      }))
      if (!tabs.value.find((t) => t.id === activeTab.value)) {
        activeTab.value = tabs.value[0]?.id ?? ''
      }
    }
  } catch (e) {
    console.warn('[SolutionsView] API 请求失败，使用本地默认数据：', e)
  }
}

onMounted(loadData)
</script>

<style scoped>
.solutions-page {
  min-height: 100vh;
  background-color: #f8f9fa;
}

.sub-nav {
  background-color: #fff;
  border-bottom: 1px solid #eaeaea;
  padding: 0 20px;
}

.nav-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  gap: 30px;
}

.tab-item {
  padding: 15px 0;
  cursor: pointer;
  color: #666;
  position: relative;
}

.tab-item.active {
  color: #0056ff;
  font-weight: bold;
}
.tab-item.active::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: #0056ff;
}

.content-area {
  max-width: 1200px;
  margin: 40px auto;
}
</style>
