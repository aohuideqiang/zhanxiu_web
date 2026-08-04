<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { HomeFilled, Money, ShoppingCart, Van, Warning } from '@element-plus/icons-vue'
import { ElImage } from 'element-plus'
import { api, type Solution } from '../api'
import { solutions as fallbackSolutions } from '../content/landingPage.ts'

// 解决方案数据项类型
type SolutionItem = {
  key: string
  title: string
  description: string
  tags: readonly string[]
  icon?: unknown
  image?: string
  iconIsImage?: boolean
}

// 后端地址，用于拼接相对路径图片
const BACKEND_URL = 'http://localhost:8080'

// 图标名称 → Element Plus 组件映射
const iconMap: Record<string, unknown> = {
  Van,
  HomeFilled,
  Warning,
  Money,
  ShoppingCart,
}

// 响应式数据：先用 fallback 兜底，后端加载成功后覆盖
const solutions = ref<SolutionItem[]>([...fallbackSolutions] as SolutionItem[])
// 当前选中的解决方案 key，默认第一个
const activeSolution = ref<string>(solutions.value[0]?.key ?? 'automotive')

// 当前激活的解决方案数据（根据 key 计算）
const activeSolutionData = computed<SolutionItem>(
  () => solutions.value.find((item) => item.key === activeSolution.value) ?? solutions.value[0]!,
)

// 解析图片 URL：支持 http、base64、相对路径等多种格式
function resolveImageUrl(url: string): string {
  if (!url) return ''
  if (url.startsWith('data:') || url.startsWith('http')) return url
  if (url.startsWith('/')) return BACKEND_URL + url
  if (url.startsWith('upload') || url.startsWith('file')) return BACKEND_URL + '/' + url
  if (/^[A-Za-z0-9+/=]{100,}$/.test(url)) return 'data:image/jpeg;base64,' + url
  return BACKEND_URL + '/' + url
}

// 检查是否为图片 URL
function isImageUrl(url: string): boolean {
  if (!url) return false
  return (
    url.startsWith('data:') ||
    url.startsWith('http') ||
    url.startsWith('/') ||
    url.startsWith('upload')
  )
}

// 将后端返回的 Solution 转换为前端可用格式
function mapSolution(item: Solution): SolutionItem {
  // 处理 icon：如果 iconUrl 存在且是有效图片 URL，则直接使用；否则使用默认图标组件
  let mappedIcon: unknown = iconMap.Van
  let iconIsImage = false

  if (item.iconUrl && isImageUrl(item.iconUrl)) {
    mappedIcon = resolveImageUrl(item.iconUrl)
    iconIsImage = true
  } else if (item.iconUrl && item.iconUrl in iconMap) {
    mappedIcon = iconMap[item.iconUrl]
  }

  // 兼容 tags：后端可能返回逗号分隔字符串或数组
  let tags: string[] = []
  if (Array.isArray(item.tags)) {
    tags = item.tags
  } else if (typeof item.tags === 'string' && item.tags.trim()) {
    tags = item.tags
      .split(',')
      .map((s) => s.trim())
      .filter(Boolean)
  }

  return {
    key: item.solutionKey || '',
    title: item.title,
    description: item.description,
    tags,
    icon: mappedIcon,
    iconIsImage,
    image: resolveImageUrl(item.imageUrl ?? ''),
  }
}

// 组件挂载时自动从后端加载数据
onMounted(async () => {
  try {
    const data = await api.getSolutions()
    if (data?.length) {
      solutions.value = data.map(mapSolution)
      // 同步 activeSolution，确保指向加载后的第一个解决方案
      const firstKey = solutions.value[0]?.key
      if (firstKey && !solutions.value.find((s) => s.key === activeSolution.value)) {
        activeSolution.value = firstKey
      }
      console.log('[SolutionSection] ✅ 解决方案数据加载成功:', solutions.value.length, '条')
    }
  } catch {
    console.warn('[SolutionSection] ❌ 解决方案数据加载失败，使用默认数据')
  }
})
</script>

<template>
  <!-- 数据解决方案板块 -->
  <section id="solutions" class="solutions">
    <!-- 最大宽度容器，防止宽屏下无限拉伸 -->
    <div class="content-wrapper">
      <!-- 板块标题 -->
      <div class="section-title center">
        <span>数据解决方案</span>
        <h2>面向多行业的数据驱动解决方案，助力业务智能化升级</h2>
      </div>

      <!-- Tab 切换区：使用 el-radio-group 实现，居中排列 -->
      <el-radio-group v-model="activeSolution" class="solution-radio-group" size="large">
        <el-radio-button
          v-for="solution in solutions"
          :key="solution.key"
          :label="solution.title"
          :value="solution.key"
        />
      </el-radio-group>

      <!-- 图文分栏布局：左右各占一半，垂直居中 -->
      <el-row :gutter="40" class="solution-content" align="middle">
        <!-- 左侧图片区：受控尺寸，防止溢出 -->
        <el-col :xs="24" :md="12" class="solution-image-col">
          <el-image :src="activeSolutionData.image" fit="cover" class="solution-image" />
        </el-col>

        <!-- 右侧内容区：图标 + 标题 + 描述 + 按钮 -->
        <el-col :xs="24" :md="12" class="solution-info-col">
          <div class="solution-info">
            <!-- 标题行：蓝底白色圆形图标 + 动态标题 -->
            <div class="solution-title-row">
              <div
                class="solution-icon-bg"
                :class="{ 'solution-icon-bg--image': activeSolutionData.iconIsImage }"
              >
                <!-- 如果是图片 URL，使用 img 渲染 -->
                <img
                  v-if="activeSolutionData.iconIsImage"
                  :src="activeSolutionData.icon as string"
                  :alt="activeSolutionData.title"
                  class="solution-icon-img"
                />
                <!-- 如果是组件，使用 el-icon 渲染 -->
                <el-icon v-else>
                  <component :is="activeSolutionData.icon" />
                </el-icon>
              </div>
              <h3>{{ activeSolutionData.title }}</h3>
            </div>

            <!-- 描述文本：动态绑定，行高 1.8，深灰色，两端对齐 -->
            <p class="solution-desc">{{ activeSolutionData.description }}</p>

            <!-- 标签区：动态渲染标签列表 -->
            <div class="solution-tags">
              <el-tag v-for="tag in activeSolutionData.tags" :key="tag" effect="plain" size="large">
                {{ tag }}
              </el-tag>
            </div>

            <!-- 按钮组：查看方案 + 提交需求 -->
            <div class="solution-actions">
              <el-button>查看方案</el-button>
              <el-button type="primary">提交需求</el-button>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </section>
</template>

<style scoped>
/* ========== 数据解决方案板块样式 ========== */
.solutions {
  margin-top: 72px;
  padding: 0 40px;
}

/* 最大宽度容器，居中对齐，防止宽屏下无限拉伸 */
.content-wrapper {
  max-width: 1200px;
  margin: 0 auto;
}

/* 板块标题 */
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

/* Tab 切换区容器 */
.solution-radio-group {
  display: flex;
  justify-content: center;
  margin-bottom: 60px;
}

/* 图文分栏布局容器 */
.solution-content {
  margin-top: 0;
}

/* 左侧图片列：居中对齐 */
.solution-image-col {
  display: flex;
  justify-content: center;
}

/* 图片受控缩放：固定高度 + cover + 圆角，防止撑破栅格 */
.solution-image {
  width: 100%;
  height: 350px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.solution-image :deep(.el-image__inner) {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
}

/* 右侧内容区：垂直排列，垂直居中 */
.solution-info {
  display: flex;
  flex-direction: column;
  gap: 20px;
  height: 100%;
  justify-content: center;
}

/* 标题行：图标 + 文字 水平排列 */
.solution-title-row {
  display: flex;
  align-items: center;
  gap: 16px;
}

.solution-title-row h3 {
  margin: 0;
  font-size: 24px;
  font-weight: 700;
  color: #000;
}

/* 蓝底白色圆形图标 */
.solution-icon-bg {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: #409eff;
  color: #fff;
  display: grid;
  place-items: center;
  font-size: 24px;
  flex-shrink: 0;
}

.solution-icon-bg .el-icon {
  color: #fff;
}

/* 图片类型的 icon 样式 */
.solution-icon-bg--image {
  overflow: hidden;
  padding: 0;
  background: #f0f4fa;
}

.solution-icon-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 50%;
}

/* 描述文本：深灰色、行高 1.8、两端对齐 */
.solution-desc {
  color: #666;
  line-height: 1.8;
  font-size: 18px;
  margin: 0;
  text-align: justify;
}

/* 标签区：水平排列，自动换行 */
.solution-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

/* 按钮组：查看方案 + 提交需求 */
.solution-actions {
  display: flex;
  gap: 14px;
}

/* ========== 自定义 Radio-Button 样式 ========== */
/* 未选中：白底、黑字、浅色边框 */
:deep(.el-radio-button__inner) {
  background-color: #fff;
  color: #333;
  border: 1px solid #dcdfe6;
  box-shadow: none;
}

/* 选中：白底、蓝字、蓝色边框（非蓝色实心背景） */
:deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) {
  background-color: #fff;
  color: #409eff;
  border-color: #409eff;
  box-shadow: -1px 0 0 0 #409eff;
}

/* hover 状态：蓝字、蓝边框 */
:deep(.el-radio-button__inner:hover) {
  color: #409eff;
  border-color: #409eff;
}

/* 去掉首选项和尾选项的圆角（改为直角风格） */
:deep(.el-radio-button:first-child .el-radio-button__inner) {
  border-radius: 4px 0 0 4px;
}

:deep(.el-radio-button:last-child .el-radio-button__inner) {
  border-radius: 0 4px 4px 0;
}

/* 只有一个选项时的圆角 */
:deep(.el-radio-button__original-radio:only-child + .el-radio-button__inner) {
  border-radius: 4px;
}

/* 标签样式放大 */
:deep(.el-tag) {
  color: #ffffff;
  background-color: rgb(51, 117, 185);
  font-size: 20px;
}

/* 响应式：小屏下图片列增加底部间距 */
@media (max-width: 960px) {
  .solution-image-col {
    margin-bottom: 24px;
  }

  .solutions,
  .section-title {
    padding-left: 16px;
    padding-right: 16px;
  }
}
</style>
