import {
  HomeFilled,
  Money,
  Monitor,
  Platform,
  Search,
  ShoppingCart,
  Van,
  Warning,
} from '@element-plus/icons-vue'

export const navigationItems = [
  { label: '首页', href: '/' },
  { label: '解决方案', href: '/solutions' },
  { label: '我们的服务', href: '/services' },
  { label: '联系我们', href: '/contact' },
]

export const heroContent = {
  tag: '智能驾驶 · 数据服务',
  title: '广西展秀信息科技有限公司',
  description:
    '先用 Vue + Element Plus 做出一个“可继续扩展”的首页骨架，后续你可以在这个基础上逐步补齐后端接口、表单提交和真实业务内容。',
  actions: ['立即咨询', '查看案例'],
  metrics: [
    { value: '10+', label: '常见数据类型' },
    { value: '3 类', label: '核心服务模块' },
    { value: '100%', label: '前后端分离架构' },
  ],
}

export const heroSlides = [
  {
    id: 1,
    badge: '一站式数据服务',
    title: '一站式数据服务',
    description: '从数据采集、数据标注、平台私有化部署，到标注驻场服务，提供一站式数据服务',
    imageUrl:
      'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1200 760"><defs><linearGradient id="g" x1="0" x2="1" y1="0" y2="1"><stop offset="0%" stop-color="%23dfe9f7"/><stop offset="100%" stop-color="%23f7fbff"/></linearGradient></defs><rect width="1200" height="760" fill="url(%23g)"/><circle cx="900" cy="180" r="140" fill="%23c9d9ef" opacity="0.8"/><rect x="120" y="170" width="420" height="300" rx="28" fill="%23ffffff" opacity="0.96"/><rect x="160" y="220" width="240" height="20" rx="10" fill="%23222222" opacity="0.18"/><rect x="160" y="268" width="320" height="18" rx="9" fill="%23222222" opacity="0.12"/><rect x="160" y="310" width="280" height="18" rx="9" fill="%23222222" opacity="0.12"/><rect x="160" y="360" width="180" height="44" rx="22" fill="%23222222" opacity="0.14"/><text x="160" y="520" font-size="44" font-family="Arial" fill="%23111111" opacity="0.72">AI DATA PLATFORM</text></svg>',
  },
  {
    id: 2,
    badge: '场景采集',
    title: '高质量场景采集',
    description: '把你的行业图片、案例图、项目实拍图统一放进轮播。',
    imageUrl:
      'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1200 760"><defs><linearGradient id="g" x1="0" x2="1" y1="0" y2="1"><stop offset="0%" stop-color="%23edf3fb"/><stop offset="100%" stop-color="%23fdfefe"/></linearGradient></defs><rect width="1200" height="760" fill="url(%23g)"/><rect x="110" y="120" width="980" height="520" rx="38" fill="%23ffffff" opacity="0.95"/><path d="M160 520 C280 390, 380 360, 490 430 S740 610, 940 430 S1080 320, 1100 340" fill="none" stroke="%23000000" stroke-opacity="0.15" stroke-width="18" stroke-linecap="round"/><circle cx="290" cy="280" r="78" fill="%23d8e6f7"/><circle cx="560" cy="260" r="110" fill="%23dde8f4"/><circle cx="860" cy="310" r="92" fill="%23dfeaf8"/><text x="170" y="180" font-size="38" font-family="Arial" fill="%23111111" opacity="0.74">SCENE COLLECTION</text></svg>',
  },
  {
    id: 3,
    badge: 'API 就绪',
    title: '后端接口可替换',
    description: '后端只要返回图片 URL，前端轮播就能直接替换。',
    imageUrl:
      'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1200 760"><defs><linearGradient id="g" x1="0" x2="1" y1="0" y2="1"><stop offset="0%" stop-color="%23eef5fd"/><stop offset="100%" stop-color="%23f7f9fc"/></linearGradient></defs><rect width="1200" height="760" fill="url(%23g)"/><rect x="140" y="150" width="920" height="460" rx="36" fill="%23ffffff" opacity="0.96"/><rect x="220" y="220" width="360" height="22" rx="11" fill="%23222222" opacity="0.15"/><rect x="220" y="270" width="500" height="16" rx="8" fill="%23222222" opacity="0.12"/><rect x="220" y="310" width="460" height="16" rx="8" fill="%23222222" opacity="0.12"/><rect x="220" y="390" width="240" height="72" rx="20" fill="%23dbe7f6"/><rect x="490" y="390" width="240" height="72" rx="20" fill="%23eef3f9"/><text x="220" y="540" font-size="38" font-family="Arial" fill="%23111111" opacity="0.74">API READY</text></svg>',
  },
]

export const overviewCards = [
  {
    icon: Search,
    title: '真实场景采集',
    text: '围绕业务场景搭建采集流程，尽量接近附件里的内容结构。',
  },
  {
    icon: Monitor,
    title: '多类型标注',
    text: '支持 2D、3D、语义分割、关键点等常见标注能力。',
  },
  {
    icon: Platform,
    title: '平台化交付',
    text: '通过统一平台管理任务、审核和交付，便于后续扩展后端。',
  },
]

export const solutions = [
  {
    key: 'automotive',
    title: '智能驾驶',
    description: '面向自动驾驶、车道线、3D 点云和 DMS 场景，提供采集、标注、质检一体化方案。',
    tags: ['车道线标注', '3D 点云', '语义分割'],
    icon: Van,
    image: 'https://picsum.photos/seed/auto/600/400',
  },
  {
    key: 'smart-home',
    title: '智能家居',
    description: '围绕家庭场景的目标检测、交互识别与设备联动数据，支持定制化采集。',
    tags: ['目标检测', '行为识别', '设备联动'],
    icon: HomeFilled,
    image: 'https://picsum.photos/seed/home/600/400',
  },
  {
    key: 'security',
    title: '智能安防',
    description: '为安防巡检、异常识别、身份核验等场景提供高质量训练数据。',
    tags: ['异常检测', '人脸属性', '事件分析'],
    icon: Warning,
    image: 'https://picsum.photos/seed/security/600/400',
  },
  {
    key: 'finance',
    title: '智能金融',
    description: '面向风控、反欺诈、客户画像等金融场景，提供可信的数据服务支持。',
    tags: ['风控建模', '反欺诈', '客户画像'],
    icon: Money,
    image: 'https://picsum.photos/seed/finance/600/400',
  },
  {
    key: 'retail',
    title: '新零售',
    description: '针对线上线下零售场景，提供商品识别、客流分析等核心数据解决方案。',
    tags: ['商品识别', '客流分析', '货架检测'],
    icon: ShoppingCart,
    image: 'https://picsum.photos/seed/retail/600/400',
  },
] as const

export const serviceHighlights = [
  {
    title: '前端采用 Vue 3 + Element Plus',
    value: '01',
  },
  {
    title: '后续可直接接入你的 API',
    value: '02',
  },
  {
    title: '当前版本已完成页面骨架',
    value: '03',
  },
]

export const contactContent = {
  title: '如果你愿意，我可以继续把它做得更像附件',
  description: '下一步可以继续补：轮播、下拉菜单、表单弹窗、底部页脚和响应式细节。',
  buttonText: '立即咨询',
}

export const footerContent = {
  // 左侧公司信息区
  logo: '',
  brand: '广西展秀信息科技有限公司',
  slogan: '高质量AI数据',
  address: '南宁市高新区科园大道95号',
  phone: '400-888-1234',
  email: 'admin@zanxiu.com',
  // 中间导航链接区
  navGroups: [
    {
      title: '产品服务',
      links: ['数据采集', '数据标注', '质量质检', '平台部署'],
    },
    {
      title: '解决方案',
      links: ['智能驾驶', '智能家居', '智能安防', '智能金融', '新零售'],
    },
    {
      title: '资源与生态',
      links: ['开发文档', '技术博客', '合作伙伴'],
    },
    {
      title: '关于我们',
      links: ['公司简介', '加入我们', '媒体报道', '商务合作'],
    },
  ],
  // 右侧二维码与友链区
  qrcode:
    'https://api.qrserver.com/v1/create-qr-code/?size=180x180&data=https://zanxiu.com',
  qrcodeTip: '友情链接：云测平台',
  // 底部版权
  copyright: 'Copyright © 2026 广西展秀信息科技有限公司. All rights reserved.',
}
