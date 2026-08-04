import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as Icons from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'
import './styles/global.scss'

const app = createApp(App)
app.use(ElementPlus)
app.use(router)
// 注册全部图标，模板里直接用 <el-icon><Aim /></el-icon>
for (const [name, comp] of Object.entries(Icons)) {
  app.component(name, comp)
}
app.mount('#app')
