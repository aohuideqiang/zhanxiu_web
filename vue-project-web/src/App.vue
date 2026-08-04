<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { api } from './api'
import type { BrandInfo, NavigationItem } from './api'
import { navigationItems as fallbackNav } from './content/landingPage'

const navigation = ref<NavigationItem[]>(fallbackNav as NavigationItem[])
const brand = ref<BrandInfo>({
  title: '广西展秀信息科技有限公司',
  subtitle: 'AI数据解决方案',
  mark: 'AI',
})

async function loadData() {
  try {
    const [navData, brandData] = await Promise.all([
      api.getNavigation(),
      api.getBrand(),
    ])
    if (navData?.length) navigation.value = navData
    if (brandData) brand.value = brandData
  } catch (e) {
    console.warn('[App] API 请求失败，使用本地默认数据：', e)
  }
}

onMounted(loadData)
</script>

<template>
  <div class="app-shell">
    <header class="topbar">
      <div class="brand">
        <div class="brand-mark">{{ brand.mark }}</div>
        <div>
          <div class="brand-title">{{ brand.title }}</div>
          <div class="brand-subtitle">{{ brand.subtitle }}</div>
        </div>
      </div>

      <nav class="nav-links">
        <router-link v-for="item in navigation" :key="item.href" :to="item.href">{{
          item.label
        }}</router-link>
      </nav>

      <el-button class="topbar-action" type="primary" round>获取方案</el-button>
    </header>

    <router-view />
  </div>
</template>

<style>
.app-shell {
  min-height: 100vh;
  width: 100%;
}

.topbar {
  width: 100%;
  padding: 20px 32px;
  display: flex;
  align-items: center;
  position: sticky;
  top: 0;
  z-index: 1000;
  backdrop-filter: blur(22px) saturate(140%);
  background:
    linear-gradient(180deg, rgba(10, 14, 22, 0.88) 0%, rgba(14, 19, 30, 0.72) 100%),
    radial-gradient(circle at top left, rgba(120, 165, 255, 0.14), transparent 26%);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.18);
}

.brand {
  display: flex;
  align-items: center;
  gap: 18px;
  position: relative;
  z-index: 2;
}

.nav-links {
  display: flex;
  gap: 34px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  z-index: 1;
}

.brand-mark {
  width: 60px;
  height: 60px;
  border-radius: 20px;
  display: grid;
  place-items: center;
  font-size: 20px;
  font-weight: 700;
  color: #000;
  background: linear-gradient(135deg, #edf3fb, #cfd9ea);
  box-shadow: 0 16px 34px rgba(0, 0, 0, 0.18);
}

.brand-title {
  font-size: 22px;
  font-weight: 700;
  color: #fff;
}

.brand-subtitle {
  color: rgba(255, 255, 255, 0.7);
  font-size: 14px;
}

.nav-links {
  display: flex;
  gap: 34px;
}

.nav-links a {
  color: rgba(255, 255, 255, 0.9);
  text-decoration: none;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 0.08em;
}

.topbar-action {
  color: #fff;
  margin-left: auto;
  position: relative;
  z-index: 2;
}
</style>
