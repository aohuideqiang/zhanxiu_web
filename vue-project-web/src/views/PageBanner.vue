<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref, watch } from 'vue'

interface BannerFrame {
  image: string
  title: string
  desc?: string
  badge?: string
  actions?: { text: string; onClick: () => void }[]
}

const props = withDefaults(
  defineProps<{
    slides?: BannerFrame[]
    image?: string
    title?: string
    desc?: string
    badge?: string
    actions?: { text: string; onClick: () => void }[]
    height?: string
  }>(),
  {
    slides: () => [],
    image: '',
    title: '',
    desc: '',
    badge: '',
    actions: () => [],
    height: '100vh',
  },
)

const single = computed<BannerFrame>(() => ({
  image: props.image,
  title: props.title,
  desc: props.desc,
  badge: props.badge,
  actions: props.actions,
}))

const list = computed<BannerFrame[]>(() =>
  props.slides.length > 0 ? props.slides : [single.value],
)

const resolvedList = computed<BannerFrame[]>(() =>
  list.value.map((f) => ({ ...f, image: resolveImageUrl(f.image) })),
)

function resolveImageUrl(url: string): string {
  if (!url) return ''
  if (url.startsWith('data:') || url.startsWith('http') || url.startsWith('/')) return url
  return '/' + url
}

const isCarousel = computed(() => resolvedList.value.length > 1)

const current = ref(0)
let timer: ReturnType<typeof setInterval> | null = null

const next = () => {
  if (!isCarousel.value) return
  current.value = (current.value + 1) % resolvedList.value.length
}
const prev = () => {
  if (!isCarousel.value) return
  current.value = (current.value - 1 + resolvedList.value.length) % resolvedList.value.length
}
const goTo = (i: number) => {
  if (!isCarousel.value) return
  current.value = i
}

const startAutoPlay = () => {
  if (timer) return
  timer = setInterval(next, 5000)
}
const stopAutoPlay = () => {
  if (timer) {
    clearInterval(timer)
    timer = null
  }
}

watch(resolvedList, () => {
  if (current.value >= resolvedList.value.length) current.value = 0
})

const brokenImages = ref<Set<number>>(new Set())
function onImageError(i: number) {
  brokenImages.value.add(i)
}

onMounted(() => {
  if (isCarousel.value) startAutoPlay()
})

onUnmounted(() => {
  stopAutoPlay()
})
</script>

<template>
  <section
    class="pb"
    :class="{ 'pb--carousel': isCarousel }"
    :style="{ height: height }"
    @mouseenter="stopAutoPlay"
    @mouseleave="startAutoPlay"
  >
    <div
      v-for="(f, i) in resolvedList"
      :key="i"
      class="pb__frame"
      :class="{ 'is-active': i === current }"
    >
      <img
        v-if="f.image && !brokenImages.has(i)"
        class="pb__img"
        :src="f.image"
        :alt="f.title"
        @error="onImageError(i)"
      />
      <div class="pb__veil"></div>

      <div class="pb__center">
        <slot name="content" :slide="f" :index="i">
          <span v-if="f.badge" class="pb__badge">{{ f.badge }}</span>
          <h1 class="pb__title">{{ f.title }}</h1>
          <p v-if="f.desc" class="pb__desc">{{ f.desc }}</p>
          <div v-if="f.actions && f.actions.length" class="pb__actions">
            <button
              v-for="(a, ai) in f.actions"
              :key="ai"
              class="pb__btn"
              :class="{ 'pb__btn--primary': ai === f.actions.length - 1 }"
              @click="a.onClick"
            >
              {{ a.text }}
            </button>
          </div>
        </slot>
      </div>
    </div>

    <button v-if="isCarousel" class="pb__arrow pb__arrow--l" @click="prev">‹</button>
    <button v-if="isCarousel" class="pb__arrow pb__arrow--r" @click="next">›</button>

    <div v-if="isCarousel" class="pb__dots">
      <i
        v-for="(_, i) in resolvedList"
        :key="i"
        :class="{ on: i === current }"
        @click="goTo(i)"
      ></i>
    </div>
  </section>
</template>

<style scoped>
.pb {
  position: relative;
  overflow: hidden;
  background: #060c18;
  min-height: 320px;
}

.pb__frame {
  position: absolute;
  inset: 0;
  opacity: 0;
  transition: opacity 0.8s ease;
  pointer-events: none;
}
.pb__frame.is-active {
  opacity: 1;
  pointer-events: auto;
}

.pb__img {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  object-fit: cover;
  object-position: center;
}
.pb__frame.is-active .pb__img {
  animation: pb-kenburns 7s ease-out forwards;
}
@keyframes pb-kenburns {
  from {
    transform: scale(1.08);
  }
  to {
    transform: scale(1);
  }
}

.pb__veil {
  position: absolute;
  inset: 0;
  z-index: 1;
  background: linear-gradient(180deg, rgba(6, 12, 24, 0.45), rgba(6, 12, 24, 0.65));
}

.pb__center {
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

.pb__frame.is-active .pb__badge,
.pb__frame.is-active .pb__title,
.pb__frame.is-active .pb__desc,
.pb__frame.is-active .pb__actions {
  animation: pb-rise 0.8s ease both;
}
.pb__frame.is-active .pb__title {
  animation-delay: 0.08s;
}
.pb__frame.is-active .pb__desc {
  animation-delay: 0.16s;
}
.pb__frame.is-active .pb__actions {
  animation-delay: 0.24s;
}

@keyframes pb-rise {
  from {
    opacity: 0;
    transform: translateY(24px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.pb__badge {
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

.pb__title {
  margin: 0;
  font:
    700 clamp(1.8rem, 4vw, 3rem) / 1.2 'Noto Sans SC',
    system-ui,
    sans-serif;
  letter-spacing: 0.02em;
  text-shadow: 0 2px 24px rgba(0, 0, 0, 0.5);
}

.pb__desc {
  max-width: 760px;
  margin: 18px 0 0;
  font:
    400 clamp(0.9rem, 1.4vw, 1.05rem) / 1.7 'Noto Sans SC',
    sans-serif;
  color: rgba(255, 255, 255, 0.85);
}

.pb__actions {
  margin-top: 34px;
  display: flex;
  gap: 18px;
}

.pb__btn {
  padding: 13px 38px;
  font:
    500 15px / 1 'Noto Sans SC',
    sans-serif;
  text-decoration: none;
  border-radius: 2px;
  transition:
    transform 0.2s ease,
    background 0.2s ease,
    box-shadow 0.2s ease;
  cursor: pointer;
  border: 1px solid rgba(255, 255, 255, 0.7);
  color: #fff;
  background: transparent;
}
.pb__btn:hover {
  background: rgba(255, 255, 255, 0.12);
  transform: translateY(-2px);
}
.pb__btn--primary {
  background: #1677ff;
  border-color: #1677ff;
  color: #fff;
}
.pb__btn--primary:hover {
  background: #4096ff;
  border-color: #4096ff;
  box-shadow: 0 12px 28px -10px rgba(22, 119, 255, 0.8);
}

.pb__arrow {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  z-index: 5;
  width: 48px;
  height: 48px;
  background: transparent;
  border: none;
  color: rgba(255, 255, 255, 0.7);
  font-size: 40px;
  line-height: 1;
  cursor: pointer;
  transition:
    color 0.2s ease,
    transform 0.2s ease;
}
.pb__arrow:hover {
  color: #fff;
}
.pb__arrow--l {
  left: 18px;
}
.pb__arrow--r {
  right: 18px;
}
.pb__arrow--l:hover {
  transform: translateY(-50%) translateX(-3px);
}
.pb__arrow--r:hover {
  transform: translateY(-50%) translateX(3px);
}

.pb__dots {
  position: absolute;
  bottom: 22px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 5;
  display: flex;
  gap: 10px;
}
.pb__dots i {
  width: 9px;
  height: 9px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.4);
  cursor: pointer;
  transition:
    background 0.25s,
    width 0.25s;
}
.pb__dots i.on {
  background: #fff;
  width: 24px;
  border-radius: 5px;
}
</style>
