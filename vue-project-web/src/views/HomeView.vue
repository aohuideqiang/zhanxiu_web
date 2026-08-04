<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref, watch } from 'vue'
import { ArrowRight, Message, Monitor, Phone, Platform, Search } from '@element-plus/icons-vue'
import { api, type BannerItem, type HeroSlide } from '../api'
import SolutionSection from '../components/SolutionSection.vue'
import {
  footerContent as fallbackFooter,
  heroContent,
  heroSlides as fallbackHeroSlides,
  overviewCards as fallbackServices,
  serviceHighlights as fallbackStats,
} from '../content/landingPage.ts'

// 定义通用类型，兼容前端 fallback 和后端 API 数据格式
type HeroSlideItem = {
  id?: number
  badge?: string
  title: string
  description?: string
  imageUrl: string
  subtitle?: string
  linkUrl?: string
  sortOrder?: number
}

type ServiceItem = {
  id?: number
  icon?: unknown
  iconUrl?: string
  title: string
  text?: string
  description?: string
  targetLink?: string
}

type StatItem = {
  title: string
  value: string
}

type FooterItem = {
  logo: string
  brand: string
  slogan: string
  address: string
  phone: string
  email: string
  navGroups: Array<{ title: string; links: string[] }>
  qrcode: string
  qrcodeTip: string
  copyright: string
}

const heroSlides = ref<HeroSlideItem[]>([...fallbackHeroSlides])
const services = ref<ServiceItem[]>([...fallbackServices])
const stats = ref<StatItem[]>([...fallbackStats])
const footer = ref<FooterItem>({ ...fallbackFooter })

const currentBanner = ref(0)

const iconMap: Record<string, unknown> = {
  Search,
  Monitor,
  Platform,
}

function getServiceIcon(item: ServiceItem): unknown {
  return item.iconUrl ?? item.icon ?? ''
}
function getServiceTitle(item: ServiceItem): string {
  return item.title || ''
}

function getServiceText(item: ServiceItem): string {
  return item.description || item.text || ''
}

function isImageUrl(value: unknown): value is string {
  return (
    typeof value === 'string' &&
    (value.startsWith('data:') || value.startsWith('http') || value.startsWith('/'))
  )
}

function isKnownIconName(value: unknown): value is string {
  return typeof value === 'string' && value in iconMap
}

function isComponentIcon(value: unknown): value is boolean {
  return typeof value === 'object' && value !== null
}

const BACKEND_URL = 'http://localhost:8080'

function resolveImageUrl(url: string): string {
  if (!url) return ''
  if (url.startsWith('data:') || url.startsWith('http')) return url
  if (url.startsWith('/')) return BACKEND_URL + url
  if (url.startsWith('upload') || url.startsWith('file')) return BACKEND_URL + '/' + url
  if (/^[A-Za-z0-9+/=]{100,}$/.test(url)) return 'data:image/jpeg;base64,' + url
  return BACKEND_URL + '/' + url
}

function mapBannerToHeroSlide(banner: BannerItem): HeroSlide {
  return {
    id: banner.id,
    badge: banner.linkUrl ? '点击了解更多' : '首页轮播',
    title: banner.title,
    description: banner.subtitle ?? '',
    imageUrl: resolveImageUrl(banner.imageUrl),
    linkUrl: banner.linkUrl ?? '',
  }
}

const bannerSlides = computed(() =>
  heroSlides.value.map((s) => ({
    image: s.imageUrl,
    title: s.title,
    desc: s.description,
    badge: s.badge,
    linkUrl: s.linkUrl,
    actions: [
      { text: heroContent.actions[1]!, onClick: () => {} },
      { text: heroContent.actions[0]!, onClick: () => {} },
    ],
  })),
)

async function loadData() {
  const results = await Promise.allSettled([
    api.getBanners(),
    api.getServices(),
    api.getStats(),
    api.getFooter(),
  ])

  const [bannersResult, servicesResult, statsResult, footerResult] = results

  // 只有 banners 成功就够了
  if (bannersResult.status === 'fulfilled') {
    const bannersData = bannersResult.value
    console.log('[HomeView] ✅ 轮播数据加载成功:', bannersData.length, '条')
    heroSlides.value = bannersData
      .slice()
      .sort((a, b) => (a.sortOrder ?? 0) - (b.sortOrder ?? 0))
      .map(mapBannerToHeroSlide)
  } else {
    console.warn('[HomeView] ❌ 轮播数据加载失败，使用默认数据')
  }

  if (servicesResult.status === 'fulfilled' && servicesResult.value?.length) {
    services.value = servicesResult.value
    console.log('[HomeView] ✅ 服务卡片加载成功:', services.value.length, '条')
  }
  if (statsResult.status === 'fulfilled' && statsResult.value?.length)
    stats.value = statsResult.value
  if (footerResult.status === 'fulfilled' && footerResult.value) {
    const data = footerResult.value
    // navGroups 在数据库中是 JSON 字符串，需要解析
    if (typeof data.navGroups === 'string') {
      data.navGroups = JSON.parse(data.navGroups)
    }
    footer.value = data
  }
}

onMounted(loadData)

// 自动补全 URL 协议前缀
function normalizeUrl(url?: string): string {
  if (!url) return ''
  if (!/^https?:\/\//i.test(url)) {
    return 'http://' + url
  }
  return url
}

// 点击服务卡片跳转
function handleServiceClick(item: ServiceItem) {
  if (item.targetLink) {
    window.open(normalizeUrl(item.targetLink), '_blank')
  }
}

// 点击轮播图跳转
function handleBannerClick() {
  const currentSlides = bannerSlides.value[currentBanner.value]
  if (currentSlides?.linkUrl) {
    window.open(normalizeUrl(currentSlides.linkUrl), '_blank')
  }
}

let bannerTimer: ReturnType<typeof setInterval> | null = null
watch(
  bannerSlides,
  (slides) => {
    if (bannerTimer) clearInterval(bannerTimer)
    if (slides.length > 1) {
      bannerTimer = setInterval(() => {
        currentBanner.value = (currentBanner.value + 1) % slides.length
      }, 5000)
    }
  },
  { immediate: true },
)

onUnmounted(() => {
  if (bannerTimer) clearInterval(bannerTimer)
})
</script>

<template>
  <div class="page-shell">
    <main class="page-main">
      <section id="hero" class="hero">
        <section
          class="hero-banner"
          :style="{ cursor: bannerSlides[currentBanner]?.linkUrl ? 'pointer' : 'default' }"
          @click="handleBannerClick"
        >
          <img
            v-for="(s, i) in bannerSlides"
            v-show="i === currentBanner"
            :key="i"
            class="hero-banner__img"
            :src="s.image"
            :alt="s.title"
          />
          <div class="hero-banner__veil"></div>
          <div class="hero-banner__center">
            <span v-if="bannerSlides[currentBanner]?.badge" class="hero-banner__badge">
              {{ bannerSlides[currentBanner]?.badge }}
            </span>
            <h1 class="hero-banner__title">{{ bannerSlides[currentBanner]?.title }}</h1>
            <p v-if="bannerSlides[currentBanner]?.desc" class="hero-banner__desc">
              {{ bannerSlides[currentBanner]?.desc }}
            </p>
            <div v-if="bannerSlides[currentBanner]?.actions?.length" class="hero-banner__actions">
              <button
                v-for="(a, ai) in bannerSlides[currentBanner]?.actions"
                :key="ai"
                class="hero-banner__btn"
                :class="{
                  'hero-banner__btn--primary':
                    ai === (bannerSlides[currentBanner]?.actions?.length || 0) - 1,
                }"
                @click="a.onClick"
              >
                {{ a.text }}
              </button>
            </div>
          </div>

          <!-- 轮播指示器 -->
          <div v-if="bannerSlides.length > 1" class="hero-banner__dots">
            <i
              v-for="(_, i) in bannerSlides"
              :key="i"
              :class="{ on: i === currentBanner }"
              @click="currentBanner = i"
            ></i>
          </div>
        </section>
      </section>

      <section id="services" class="services">
        <div class="section-title center">
          <span>我们的服务</span>
          <h2>从数据采集、数据标注、平台私有化部署，到标注驻场服务，提供一站式数据服务</h2>
        </div>

        <div class="service-grid">
          <el-card
            v-for="(item, idx) in services"
            :key="idx"
            class="service-card"
            shadow="hover"
            :style="{ cursor: item.targetLink ? 'pointer' : 'default' }"
            @click="handleServiceClick(item)"
          >
            <div class="service-icon">
              <img
                v-if="isImageUrl(getServiceIcon(item))"
                :src="getServiceIcon(item) as string"
                :alt="getServiceTitle(item)"
                class="service-icon__img"
              />
              <el-icon v-else-if="isKnownIconName(getServiceIcon(item))">
                <component :is="iconMap[getServiceIcon(item) as string]" />
              </el-icon>
              <el-icon v-else-if="isComponentIcon(getServiceIcon(item))">
                <component :is="getServiceIcon(item)" />
              </el-icon>
            </div>
            <h3>{{ getServiceTitle(item) }}</h3>
            <p>{{ getServiceText(item) }}</p>
            <div class="service-foot">
              <span>查看详情</span>
              <el-icon><ArrowRight /></el-icon>
            </div>
          </el-card>
        </div>
      </section>

      <!-- 数据解决方案板块（独立组件） -->
      <SolutionSection />

      <section class="stats">
        <el-card class="stats-card" shadow="never">
          <div class="stats-grid">
            <div v-for="item in stats" :key="item.value">
              <strong>{{ item.value }}</strong>
              <span>{{ item.title }}</span>
            </div>
          </div>
        </el-card>
      </section>

      <footer class="site-footer">
        <div class="footer-inner">
          <!-- 左侧：公司信息区 -->
          <div class="footer-left">
            <div class="footer-logo">
              <img v-if="footer.logo" :src="footer.logo" :alt="footer.brand" />
              <span v-else class="footer-logo-text">{{ footer.brand }}</span>
            </div>
            <div class="footer-slogan">{{ footer.slogan }}</div>
            <div class="footer-address">{{ footer.address }}</div>
            <div class="footer-contacts">
              <div class="footer-contact-item">
                <el-icon><Phone /></el-icon>
                <span>{{ footer.phone }}</span>
              </div>
              <div class="footer-contact-item">
                <el-icon><Message /></el-icon>
                <span>{{ footer.email }}</span>
              </div>
            </div>
          </div>

          <!-- 中间：导航链接区 -->
          <div class="footer-nav">
            <div v-for="group in footer.navGroups" :key="group.title" class="footer-nav-group">
              <h4>{{ group.title }}</h4>
              <a v-for="link in group.links" :key="link" href="#" class="footer-nav-link">{{
                link
              }}</a>
            </div>
          </div>

          <!-- 右侧：二维码与友链区 -->
          <div class="footer-right">
            <img v-if="footer.qrcode" :src="footer.qrcode" alt="二维码" class="footer-qrcode" />
            <p class="footer-qrcode-tip">{{ footer.qrcodeTip }}</p>
          </div>
        </div>

        <div class="footer-bottom">
          {{ footer.copyright }}
        </div>
      </footer>
    </main>
  </div>
</template>

<style scoped>
.page-shell {
  width: 100%;
  min-height: 100vh;
  color: #000;
  background:
    radial-gradient(circle at top left, rgba(10, 132, 255, 0.08), transparent 28%),
    linear-gradient(180deg, #ffffff 0%, #f7f9fc 100%);
}

.topbar {
  width: 100%;
  padding: 22px 28px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.page-main {
  width: 100%;
  padding: 0;
}

.hero {
  position: relative;
  margin-top: 0;
}

.hero-banner {
  position: relative;
  width: 100%;
  height: 55vh;
  min-height: 320px;
  overflow: hidden;
  background: #060c18;
}

.hero-banner__img {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
  z-index: 0;
}

.hero-banner__veil {
  position: absolute;
  inset: 0;
  z-index: 1;
  background: linear-gradient(180deg, rgba(6, 12, 24, 0.45), rgba(6, 12, 24, 0.65));
}

.hero-banner__center {
  position: relative;
  z-index: 2;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  color: #fff;
  padding: 0 24px;
}

.hero-banner__badge {
  display: inline-flex;
  width: fit-content;
  margin-bottom: 18px;
  padding: 7px 16px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.12);
  border: 1px solid rgba(255, 255, 255, 0.22);
  backdrop-filter: blur(8px);
  color: rgba(255, 255, 255, 0.92);
  font-size: 13px;
  letter-spacing: 0.08em;
}

.hero-banner__title {
  margin: 0;
  font:
    700 clamp(2rem, 5vw, 3.5rem) / 1.25 'Noto Sans SC',
    system-ui,
    sans-serif;
  letter-spacing: 0.04em;
  text-shadow: 0 2px 24px rgba(0, 0, 0, 0.55);
}

.hero-banner__desc {
  max-width: 860px;
  margin: 20px 0 0;
  font:
    400 clamp(1rem, 1.6vw, 1.2rem) / 1.8 'Noto Sans SC',
    sans-serif;
  color: rgba(255, 255, 255, 0.88);
}

.hero-banner__actions {
  margin-top: 34px;
  display: flex;
  gap: 18px;
}

.hero-banner__btn {
  padding: 13px 38px;
  font:
    500 15px / 1 'Noto Sans SC',
    sans-serif;
  border-radius: 2px;
  cursor: pointer;
  border: 1px solid rgba(255, 255, 255, 0.7);
  color: #fff;
  background: transparent;
  transition:
    transform 0.2s ease,
    background 0.2s ease;
}
.hero-banner__btn:hover {
  background: rgba(255, 255, 255, 0.12);
}
.hero-banner__btn--primary {
  background: #1677ff;
  border-color: #1677ff;
}
.hero-banner__btn--primary:hover {
  background: #4096ff;
}

.hero-banner__dots {
  position: absolute;
  bottom: 22px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 5;
  display: flex;
  gap: 10px;
}
.hero-banner__dots i {
  width: 9px;
  height: 9px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.4);
  cursor: pointer;
  transition:
    background 0.25s,
    width 0.25s;
  display: block;
}
.hero-banner__dots i.on {
  background: #fff;
  width: 24px;
  border-radius: 5px;
}

.panel-header,
.service-foot {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.panel-grid {
  display: grid;
  gap: 14px;
}

.panel-card {
  padding: 16px;
  border-radius: 18px;
  background: linear-gradient(180deg, #f7fbff 0%, #eef5ff 100%);
  color: #000;
}

.panel-card .el-icon,
.service-icon {
  font-size: 22px;
  color: #000;
}

.panel-card h3,
.service-card h3 {
  margin: 12px 0 8px;
  font-size: 18px;
  color: #000;
}

.panel-card p,
.service-card p {
  color: rgba(0, 0, 0, 0.72);
  line-height: 1.7;
}

.section-title {
  margin: 48px 0 22px;
  padding: 0 40px;
}

.section-title.center {
  text-align: center;
}

.section-title span {
  display: inline-block;
  margin-bottom: 12px;
  font-size: clamp(1.8rem, 3vw, 2.5rem);
  font-weight: 700;
  letter-spacing: 0.04em;
  color: #000;
}

.section-title h2 {
  font-size: clamp(0.9rem, 1.2vw, 1.05rem);
  font-weight: 400;
  line-height: 1.7;
  color: rgba(0, 0, 0, 0.65);
  margin: 0;
}

.stats-card {
  border: 0;
  border-radius: 28px;
}

.services {
  margin-top: 72px;
  padding: 0 40px;
}

.service-grid {
  margin-top: 32px;
  padding: 0;
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 24px;
}

.service-card {
  border: 0;
  border-radius: 24px;
  background: #fff;
  color: #000;
  padding: 28px 24px;
  transition:
    transform 0.25s ease,
    box-shadow 0.25s ease;
}

.service-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
}

.service-icon {
  width: 54px;
  height: 54px;
  border-radius: 18px;
  background: #eef6ff;
  display: grid;
  place-items: center;
  font-size: 26px;
  overflow: hidden;
}

.service-icon__img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 18px;
}

.service-foot {
  margin-top: 24px;
  color: #000;
  font-weight: 600;
}

.stats {
  margin-top: 72px;
  padding: 0 40px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 16px;
}

.stats-grid div {
  padding: 18px;
  border-radius: 18px;
  background: #f7fbff;
}

.stats-grid strong {
  display: block;
  font-size: 24px;
  color: #000;
  margin-bottom: 8px;
}

.stats-card {
  border-radius: 0;
}

.site-footer {
  margin-top: 0;
  padding: 48px 40px 24px;
  border-radius: 0;
  background:
    radial-gradient(circle at top left, rgba(91, 140, 229, 0.22), transparent 28%),
    linear-gradient(180deg, #121722 0%, #0b0f17 100%);
  border: 1px solid rgba(255, 255, 255, 0.08);
  box-shadow: 0 20px 44px rgba(0, 0, 0, 0.22);
  color: #e7eefc;
}

.site-footer h3,
.site-footer h4,
.site-footer p,
.site-footer a {
  color: #e7eefc;
}

/* 三栏布局容器 */
.footer-inner {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr 2fr 1fr;
  gap: 48px;
  align-items: start;
}

/* 左侧公司信息区 */
.footer-left {
  padding-right: 40px;
  border-right: 1px solid rgba(255, 255, 255, 0.12);
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.footer-logo {
  display: flex;
  align-items: center;
  gap: 10px;
}

.footer-logo img {
  height: 40px;
}

.footer-logo-text {
  font-size: 22px;
  font-weight: 700;
  letter-spacing: 0.04em;
  color: #fff;
}

.footer-slogan {
  font-size: 16px;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.9);
}

.footer-address {
  font-size: 14px;
  color: rgba(231, 238, 252, 0.65);
  line-height: 1.7;
}

.footer-contacts {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.footer-contact-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: rgba(231, 238, 252, 0.8);
}

.footer-contact-item .el-icon {
  color: #7cc0ff;
  font-size: 16px;
}

/* 中间导航链接区 */
.footer-nav {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.footer-nav-group {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.footer-nav-group h4 {
  margin: 0 0 8px;
  font-size: 15px;
  font-weight: 600;
  color: #fff;
}

.footer-nav-link {
  font-size: 13px;
  color: rgba(231, 238, 252, 0.65);
  text-decoration: none;
  transition: color 0.2s;
}

.footer-nav-link:hover {
  color: #7cc0ff;
}

/* 右侧二维码区 */
.footer-right {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.footer-qrcode {
  width: 140px;
  height: 140px;
  border-radius: 8px;
  background: #fff;
  padding: 6px;
}

.footer-qrcode-tip {
  margin: 0;
  font-size: 13px;
  color: rgba(231, 238, 252, 0.65);
}

/* 底部版权 */
.footer-bottom {
  max-width: 1200px;
  margin: 32px auto 0;
  padding-top: 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.12);
  text-align: center;
  color: rgba(231, 238, 252, 0.58);
  font-size: 13px;
}

:deep(.el-button--primary) {
  color: #000;
  background-color: #e6eef9;
  border-color: #d7e2f0;
}

:deep(.el-button--primary:hover),
:deep(.el-button--primary:focus) {
  color: #000;
  background-color: #d9e5f5;
  border-color: #cbd8ea;
}

:deep(.el-card__header),
:deep(.el-card__body),
:deep(.el-button) {
  color: #000;
}

@media (max-width: 960px) {
  .topbar {
    grid-template-columns: 1fr;
    display: grid;
  }

  .hero-copy--full {
    min-height: auto;
    padding: 24px 20px;
  }

  .topbar {
    gap: 16px;
  }

  .nav-links {
    flex-wrap: wrap;
  }

  .service-grid,
  .stats-grid {
    grid-template-columns: 1fr;
  }

  .page-main,
  .section-title,
  .service-grid,
  .stats,
  .site-footer {
    padding-left: 16px;
    padding-right: 16px;
  }

  .footer-inner {
    grid-template-columns: 1fr;
    gap: 32px;
  }

  .footer-left {
    border-right: none;
    border-bottom: 1px solid rgba(255, 255, 255, 0.12);
    padding-right: 0;
    padding-bottom: 24px;
  }

  .footer-nav {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
