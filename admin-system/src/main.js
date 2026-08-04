import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

// 引入 Element Plus 及其全局 CSS 样式
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

const app = createApp(App)

// 挂载 Element Plus 和 Vue Router
app.use(ElementPlus)
app.use(router)
app.mount('#app')