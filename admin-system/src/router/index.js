import { createRouter, createWebHistory } from 'vue-router'

// 导入所有页面组件
import Dashboard from '../views/Dashboard.vue'
import Banner from '../views/Banner.vue'
import About from '../views/About.vue'
import Product from '../views/Product.vue'
import News from '../views/News.vue'
import Solutions from '../views/Solutions.vue'
import Footer from '../views/Footer.vue'

// 路由表：路径与左侧导航栏 index 一一对应
const routes = [
  { path: '/', redirect: '/dashboard' },
  { path: '/dashboard', name: 'Dashboard', component: Dashboard },       // 服务模块
  { path: '/banner', name: 'Banner', component: Banner },               // 首页轮播
  { path: '/about', name: 'About', component: About },                  // 公司简介
  { path: '/product', name: 'Product', component: Product },            // 产品展示
  { path: '/solutions', name: 'Solutions', component: Solutions },      // 解决方案
  { path: '/footer', name: 'Footer', component: Footer },              // 页脚管理
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router