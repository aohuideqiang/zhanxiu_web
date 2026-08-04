import request from './request'

export interface HeroSlide {
  id: number
  badge: string
  title: string
  description: string
  imageUrl: string
  linkUrl?: string
}

export interface BannerItem {
  id: number
  imageUrl: string
  title: string
  subtitle?: string
  linkUrl?: string
  sortOrder?: number
}

export interface Solution {
  id?: number
  solutionKey: string
  title: string
  description: string
  tags: string[] | string // 兼容后端返回的逗号分隔字符串
  iconUrl?: string
  imageUrl?: string
}

export interface ServiceCard {
  icon: string
  title: string
  text: string
  targetLink?: string
}

export interface StatHighlight {
  value: string
  title: string
}

export interface ContactInfo {
  title: string
  description: string
  buttonText: string
}

export interface FooterLinkGroup {
  title: string
  items: string[]
}

export interface FooterContent {
  // 左侧公司信息区
  logo: string           // 企业 Logo 图片地址
  brand: string          // 企业名称
  slogan: string         // 主营业务描述
  address: string        // 公司地址
  phone: string          // 联系电话
  email: string          // 联系邮箱
  // 中间导航链接区
  navGroups: Array<{
    title: string
    links: string[]
  }>
  // 右侧二维码与友链区
  qrcode: string         // 二维码图片地址
  qrcodeTip: string      // 二维码下方提示文本
  // 底部版权
  copyright: string
}

export interface NavigationItem {
  label: string
  href: string
}

export interface BrandInfo {
  title: string
  subtitle: string
  mark: string
}

export interface SolutionSubTab {
  id: string
  name: string
}

export interface BannerData {
  image: string
  title: string
  desc: string
  badge: string
}

export const api = {
  getHeroSlides: () => request.get<unknown, HeroSlide[]>('/hero-slides'),

  getBanners: () => request.get<unknown, BannerItem[]>('/banners'),

  getSolutions: () => request.get<unknown, Solution[]>('/solutions'),

  getServices: () => request.get<unknown, ServiceCard[]>('/cards'),

  getStats: () => request.get<unknown, StatHighlight[]>('/stats'),

  getContact: () => request.get<unknown, ContactInfo>('/contact'),

  getFooter: () => request.get<unknown, FooterContent>('/footer'),

  getNavigation: () => request.get<unknown, NavigationItem[]>('/navigation'),

  getBrand: () => request.get<unknown, BrandInfo>('/brand'),

  getSolutionBanner: () => request.get<unknown, BannerData>('/solution/banner'),

  getSolutionSubTabs: () => request.get<unknown, SolutionSubTab[]>('/solution/sub-tabs'),

  submitContact: (data: { name: string; phone: string; message: string }) =>
    request.post<unknown, { success: boolean }>('/contact', data),
}
// 将所有与后端服务器交互的 HTTP 请求集中管理。通过将不同页面的数据请求（如获取轮播图、获取导航栏、提交表单）封装成 api 对象里面的各个方法，可以让项目代码更清晰、更容易维护
export default api
