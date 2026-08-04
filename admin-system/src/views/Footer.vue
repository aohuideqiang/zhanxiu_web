<script setup>
import { ref, reactive, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Delete } from '@element-plus/icons-vue'

const API_URL = 'http://localhost:8080/api/footer'

const loading = ref(true)
const submitLoading = ref(false)

const activeTab = ref('info')
const activeGroupNames = ref([0, 1, 2, 3])

// 导航链接组（可动态增删）
const navGroups = ref([
  { title: '产品服务', links: ['数据采集', '数据标注', '质量质检', '平台部署'] },
  { title: '解决方案', links: ['智能驾驶', '智能家居', '智能安防', '智能金融', '新零售'] },
  { title: '资源与生态', links: ['开发文档', '技术博客', '合作伙伴'] },
  { title: '关于我们', links: ['公司简介', '加入我们', '媒体报道', '商务合作'] }
])

const form = reactive({
  id: null,
  logo: '',
  brand: '',
  slogan: '',
  address: '',
  phone: '',
  email: '',
  qrcode: '',
  qrcodeTip: '',
  copyright: ''
})

// ========== 图片上传 ==========
const logoFileList = ref([])
const qrcodeFileList = ref([])
const readingLogo = ref(false)
const readingQrcode = ref(false)
const MAX_FILE_SIZE = 2 * 1024 * 1024

const handleLogoChange = (uploadFile) => {
  if (!uploadFile.raw || !(uploadFile.raw instanceof File)) return
  const file = uploadFile.raw
  if (!file.type.startsWith('image/')) {
    ElMessage.error('只能上传图片文件!')
    logoFileList.value = []
    form.logo = ''
    return
  }
  if (file.size > MAX_FILE_SIZE) {
    ElMessage.error('图片不能超过 2MB!')
    logoFileList.value = []
    form.logo = ''
    return
  }
  readingLogo.value = true
  const reader = new FileReader()
  reader.onload = (e) => { form.logo = e.target.result; readingLogo.value = false }
  reader.onerror = () => { readingLogo.value = false; ElMessage.error('读取失败'); logoFileList.value = [] }
  reader.readAsDataURL(file)
}

const handleQrcodeChange = (uploadFile) => {
  if (!uploadFile.raw || !(uploadFile.raw instanceof File)) return
  const file = uploadFile.raw
  if (!file.type.startsWith('image/')) {
    ElMessage.error('只能上传图片文件!')
    qrcodeFileList.value = []
    form.qrcode = ''
    return
  }
  if (file.size > MAX_FILE_SIZE) {
    ElMessage.error('图片不能超过 2MB!')
    qrcodeFileList.value = []
    form.qrcode = ''
    return
  }
  readingQrcode.value = true
  const reader = new FileReader()
  reader.onload = (e) => { form.qrcode = e.target.result; readingQrcode.value = false }
  reader.onerror = () => { readingQrcode.value = false; ElMessage.error('读取失败'); qrcodeFileList.value = [] }
  reader.readAsDataURL(file)
}

const handleLogoRemove = () => { form.logo = '' }
const handleQrcodeRemove = () => { form.qrcode = '' }

// 预览图片（在新标签页打开）
const handlePreview = (file) => {
  if (file.url) {
    window.open(file.url, '_blank')
  }
}

// ========== 导航链接组增删 ==========
const addNavGroup = () => {
  navGroups.value.push({ title: '新分组', links: ['新链接'] })
}

const removeNavGroup = (idx) => {
  navGroups.value.splice(idx, 1)
}

const addLink = (groupIdx) => {
  navGroups.value[groupIdx].links.push('新链接')
}

const removeLink = (groupIdx, linkIdx) => {
  navGroups.value[groupIdx].links.splice(linkIdx, 1)
}

// ========== 加载数据（查） ==========
const fetchData = async () => {
  loading.value = true
  try {
    const res = await axios.get(API_URL)
    const data = res.data
    Object.assign(form, data)
    if (data.navGroups) {
      const parsed = typeof data.navGroups === 'string'
        ? JSON.parse(data.navGroups)
        : data.navGroups
      navGroups.value = parsed.map(g => ({
        title: g.title,
        links: Array.isArray(g.links) ? [...g.links] : []
      }))
      activeGroupNames.value = navGroups.value.map((_, i) => i)
    }
    if (data.logo) logoFileList.value = [{ name: 'logo', url: data.logo }]
    else logoFileList.value = []
    if (data.qrcode) qrcodeFileList.value = [{ name: 'qrcode', url: data.qrcode }]
    else qrcodeFileList.value = []
  } catch (err) {
    console.error('加载失败:', err)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// ========== 保存（改/增） ==========
const handleSave = async () => {
  if (readingLogo.value || readingQrcode.value) {
    ElMessage.warning('图片正在处理中，请稍候...')
    return
  }
  submitLoading.value = true
  try {
    const payload = {
      logo: form.logo,
      brand: form.brand,
      slogan: form.slogan,
      address: form.address,
      phone: form.phone,
      email: form.email,
      navGroups: JSON.stringify(navGroups.value),
      qrcode: form.qrcode,
      qrcodeTip: form.qrcodeTip,
      copyright: form.copyright
    }
    if (form.id) {
      // 更新
      await axios.put(`${API_URL}/${form.id}`, payload)
      ElMessage.success('修改成功')
    } else {
      // 新增
      const res = await axios.post(API_URL, payload)
      form.id = res.data?.id || (await axios.get(API_URL)).data.id
      ElMessage.success('新增成功')
    }
    fetchData()
  } catch (err) {
    console.error('保存失败:', err)
    ElMessage.error('保存失败')
  } finally {
    submitLoading.value = false
  }
}

// ========== 删除 ==========
const handleDelete = () => {
  if (!form.id) {
    ElMessage.warning('没有可删除的数据')
    return
  }
  ElMessageBox.confirm(
    '确定要删除页脚内容吗？删除后将恢复默认数据。',
    '删除确认',
    {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await axios.delete(`${API_URL}/${form.id}`)
      ElMessage.success('删除成功，已恢复默认数据')
      fetchData()
    } catch (err) {
      console.error('删除失败:', err)
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// ========== 重置 ==========
const handleReset = () => {
  ElMessageBox.confirm(
    '确定要重置所有页脚内容吗？将恢复为默认数据。',
    '重置确认',
    {
      confirmButtonText: '确定重置',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    if (form.id) {
      axios.delete(`${API_URL}/${form.id}`).then(() => {
        ElMessage.success('已重置')
        fetchData()
      })
    }
  }).catch(() => {})
}

onMounted(fetchData)
</script>

<template>
  <el-card v-loading="loading">
    <template #header>
      <div class="card-header">
        <h2>页脚管理</h2>
        <div>
          <el-button @click="handleReset">重置默认</el-button>
          <el-button type="danger" plain @click="handleDelete" :disabled="!form.id">删除</el-button>
          <el-button type="primary" :loading="submitLoading" @click="handleSave">保存</el-button>
        </div>
      </div>
    </template>

    <el-tabs v-model="activeTab">
      <!-- Tab 1: 公司信息 -->
      <el-tab-pane label="公司信息" name="info">
        <el-form :model="form" label-width="120px" class="footer-form">
          <el-row :gutter="24">
            <el-col :span="12">
              <el-form-item label="Logo">
                <el-upload
                  v-model:file-list="logoFileList"
                  class="upload-demo"
                  action="#"
                  :auto-upload="false"
                  :on-change="handleLogoChange"
                  :on-remove="handleLogoRemove"
                  :on-preview="handlePreview"
                  list-type="picture"
                  :limit="1"
                  accept="image/*"
                >
                  <el-button type="primary" :disabled="logoFileList.length >= 1 || readingLogo">
                    {{ readingLogo ? '处理中...' : '点击上传 Logo' }}
                  </el-button>
                  <template #tip>
                    <div class="el-upload__tip">jpg/png 文件，大小不超过 2MB</div>
                  </template>
                </el-upload>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="品牌名称">
                <el-input v-model="form.brand" placeholder="如：广西展秀信息科技有限公司" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="24">
            <el-col :span="12">
              <el-form-item label="标语">
                <el-input v-model="form.slogan" placeholder="如：高质量AI数据" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="电话">
                <el-input v-model="form.phone" placeholder="如：400-888-1234" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="24">
            <el-col :span="12">
              <el-form-item label="邮箱">
                <el-input v-model="form.email" placeholder="如：admin@zanxiu.com" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="地址">
                <el-input v-model="form.address" placeholder="如：南宁市高新区科园大道95号" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-tab-pane>

      <!-- Tab 2: 导航链接 -->
      <el-tab-pane label="导航链接" name="nav">
        <div class="nav-toolbar">
          <span>编辑页脚的导航链接分组，点击可增删</span>
          <el-button type="primary" size="small" @click="addNavGroup">
            <el-icon><Plus /></el-icon> 新增分组
          </el-button>
        </div>

        <el-collapse v-model="activeGroupNames">
          <el-collapse-item
            v-for="(group, gIdx) in navGroups"
            :key="gIdx"
            :name="gIdx"
          >
            <template #title>
              <span class="nav-group-title">
                <el-input
                  v-model="group.title"
                  size="small"
                  style="width: 200px"
                  @click.stop
                />
              </span>
            </template>

            <div class="nav-group-content">
              <div
                v-for="(link, lIdx) in group.links"
                :key="lIdx"
                class="nav-link-row"
              >
                <el-input v-model="group.links[lIdx]" placeholder="链接文字" />
                <el-button type="danger" :icon="Delete" circle size="small" @click.stop="removeLink(gIdx, lIdx)" />
              </div>

              <el-button type="primary" link size="small" @click="addLink(gIdx)">
                <el-icon><Plus /></el-icon> 新增链接
              </el-button>

              <el-divider />

              <el-button
                type="danger"
                size="small"
                plain
                @click="removeNavGroup(gIdx)"
              >
                <el-icon><Delete /></el-icon> 删除该分组
              </el-button>
            </div>
          </el-collapse-item>
        </el-collapse>
      </el-tab-pane>

      <!-- Tab 3: 二维码与版权 -->
      <el-tab-pane label="二维码 & 版权" name="qrcode">
        <el-form :model="form" label-width="140px">
          <el-form-item label="二维码图片">
            <el-upload
              v-model:file-list="qrcodeFileList"
              class="upload-demo"
              action="#"
              :auto-upload="false"
              :on-change="handleQrcodeChange"
              :on-remove="handleQrcodeRemove"
              :on-preview="handlePreview"
              list-type="picture"
              :limit="1"
              accept="image/*"
            >
              <el-button type="primary" :disabled="qrcodeFileList.length >= 1 || readingQrcode">
                {{ readingQrcode ? '处理中...' : '点击上传二维码' }}
              </el-button>
              <template #tip>
                <div class="el-upload__tip">jpg/png 文件，建议尺寸 180×180，大小不超过 2MB</div>
              </template>
            </el-upload>
          </el-form-item>

          <el-form-item label="二维码提示">
            <el-input v-model="form.qrcodeTip" placeholder="如：友情链接：云测平台" />
          </el-form-item>

          <el-form-item label="版权信息">
            <el-input
              v-model="form.copyright"
              type="textarea"
              :rows="2"
              placeholder="如：Copyright © 2026 广西展秀信息科技有限公司. All rights reserved."
            />
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
.card-header > div:last-child { display: flex; gap: 8px; }
h2 { margin: 0; color: #303133; font-size: 18px; }
.form-tip { font-size: 12px; color: #909399; margin-top: 4px; }

.nav-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding: 12px 16px;
  background: #f5f7fa;
  border-radius: 8px;
  color: #606266;
  font-size: 14px;
}

.nav-group-title {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nav-group-content {
  padding: 0 8px;
}

.nav-link-row {
  display: flex;
  gap: 8px;
  margin-bottom: 8px;
}
</style>