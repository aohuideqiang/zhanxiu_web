import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 3000, // 将端口更改为你想要的数字
    strictPort: false, // 设为 false 表示如果 3000 还是被占用，Vite 会自动顺延尝试 3001
  }
})
