<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  Menu,
  Picture,
  OfficeBuilding,
  Goods,
  Reading,
  Files
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

// 当前激活的菜单项，根据路由路径自动同步
const activeMenu = computed(() => route.path.replace('/', ''))

// 菜单显示名映射
const menuLabels = {
  dashboard: '服务模块',
  banner: '首页轮播',
  about: '公司简介',
  product: '产品展示',
  solutions: '解决方案',
  footer: '页脚管理'
}
const currentLabel = computed(() => menuLabels[activeMenu.value] || '')

// 点击菜单项时触发路由跳转
const handleMenuSelect = (index) => {
  router.push('/' + index)
}
</script>

<template>
  <el-container class="layout-container">
    <!-- 左侧导航栏 -->
    <el-aside width="220px" class="layout-aside">
      <div class="logo-area">
        <el-icon :size="24" color="#409EFF"><Menu /></el-icon>
        <span>管理系统</span>
      </div>

      <el-menu
        :default-active="activeMenu"
        class="sidebar-menu"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        @select="handleMenuSelect"
      >
      
        <el-menu-item index="banner">
          <el-icon><Picture /></el-icon>
          <template #title>首页轮播</template>
        </el-menu-item>

        <el-menu-item index="dashboard">
          <el-icon><Menu /></el-icon>
          <template #title>服务模块</template>
        </el-menu-item>

        <el-menu-item index="solutions">
          <el-icon><Reading /></el-icon>
          <template #title>解决方案</template>
        </el-menu-item>
        
        <el-menu-item index="about">
          <el-icon><OfficeBuilding /></el-icon>
          <template #title>公司简介</template>
        </el-menu-item>

        <el-menu-item index="product">
          <el-icon><Goods /></el-icon>
          <template #title>产品展示</template>
        </el-menu-item>


        <el-menu-item index="footer">
          <el-icon><Files /></el-icon>
          <template #title>页脚管理</template>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 右侧主内容区 -->
    <el-container>
      <el-header class="layout-header">
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentLabel }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-dropdown>
            <span class="user-info">
              <el-avatar :size="32" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" />
              <span class="username">管理员</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>个人中心</el-dropdown-item>
                <el-dropdown-item>账号设置</el-dropdown-item>
                <el-dropdown-item divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.layout-container { height: 100vh; }
.layout-aside { background-color: #304156; overflow-x: hidden; }
.logo-area {
  height: 60px; display: flex; align-items: center; justify-content: center;
  gap: 10px; background-color: #2b2f3a; color: #fff; font-size: 18px; font-weight: bold;
}
.sidebar-menu { border-right: none; }
.sidebar-menu:not(.el-menu--collapse) { width: 220px; }
.layout-header {
  background-color: #fff; border-bottom: 1px solid #e6e6e6;
  display: flex; align-items: center; justify-content: space-between;
  padding: 0 20px; box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}
.header-right .user-info { display: flex; align-items: center; gap: 10px; cursor: pointer; }
.username { font-size: 14px; color: #303133; }
.layout-main { background-color: #f0f2f5; padding: 20px; }
</style>