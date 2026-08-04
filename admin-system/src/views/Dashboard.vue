<script setup>
import { ref, watch, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const API_URL = 'http://localhost:8080/api/cards'

const cards = ref([])
const loading = ref(true)

// 对话框状态
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const submitLoading = ref(false)

// 表单数据
const form = ref({
  id: null,
  iconUrl: '',
  title: '',
  description: '',
  targetLink: ''
})

// 表单校验规则
const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  targetLink: [{ required: true, message: '请输入跳转链接', trigger: 'blur' }]
}

const formRef = ref()

// ========== 图片上传相关 ==========
const fileList = ref([])
const readingImage = ref(false)

// 获取所有数据
const fetchData = async () => {
  loading.value = true
  try {
    const response = await axios.get(API_URL)
    cards.value = response.data
  } catch (error) {
    console.error('请求失败:', error)
    ElMessage.error('获取数据失败，请检查后端服务是否启动')
  } finally {
    loading.value = false
  }
}

// 重置表单
const resetForm = () => {
  form.value = {
    id: null,
    iconUrl: '',
    title: '',
    description: '',
    targetLink: ''
  }
  fileList.value = []
  readingImage.value = false
}

// 打开新增对话框
const handleAdd = () => {
  resetForm()
  isEdit.value = false
  dialogTitle.value = '新增模块'
  dialogVisible.value = true
}

// 打开编辑对话框
const handleEdit = (row) => {
  resetForm()
  isEdit.value = true
  dialogTitle.value = '编辑模块'
  form.value.id = row.id
  form.value.iconUrl = row.iconUrl
  form.value.title = row.title
  form.value.description = row.description
  form.value.targetLink = row.targetLink
  // 回显图片
  fileList.value = row.iconUrl
    ? [{ name: 'current', url: row.iconUrl }]
    : []
  dialogVisible.value = true
}

// 文件变化时：校验 + 读取为 base64
const handleChange = (uploadFile) => {
  if (!uploadFile.raw || !(uploadFile.raw instanceof File)) {
    return
  }

  const file = uploadFile.raw

  // 校验文件类型
  if (!file.type.startsWith('image/')) {
    ElMessage.error('只能上传图片文件!')
    fileList.value = []
    form.value.iconUrl = ''
    return
  }

  // 校验文件大小（2MB 以内）
  const maxSize = 2 * 1024 * 1024
  if (file.size > maxSize) {
    ElMessage.error('图片大小不能超过 2MB!')
    fileList.value = []
    form.value.iconUrl = ''
    return
  }

  // 读取为 base64
  readingImage.value = true
  const reader = new FileReader()
  reader.onload = (e) => {
    form.value.iconUrl = e.target.result
    readingImage.value = false
  }
  reader.onerror = () => {
    readingImage.value = false
    ElMessage.error('图片读取失败，请重试')
    fileList.value = []
  }
  reader.readAsDataURL(file)
}

// 移除文件
const handleRemove = () => {
  form.value.iconUrl = ''
}

// 预览图片
const handlePreview = (file) => {
  if (file.url) {
    window.open(file.url, '_blank')
  }
}

// 提交表单（新增或编辑）
const handleSubmit = async () => {
  if (readingImage.value) {
    ElMessage.warning('图片正在处理中，请稍候...')
    return
  }
  if (!formRef.value) return
  submitLoading.value = true
  try {
    await formRef.value.validate()

    const payload = {
      title: form.value.title,
      iconUrl: form.value.iconUrl,
      description: form.value.description,
      targetLink: normalizeUrl(form.value.targetLink)
    }

    if (isEdit.value) {
      await axios.put(`${API_URL}/${form.value.id}`, payload)
      ElMessage.success('修改成功')
    } else {
      await axios.post(API_URL, payload)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    fetchData()
  } catch (error) {
    if (error && error.errors) {
      ElMessage.warning('请检查表单填写')
      return
    }
    console.error('操作失败:', error)
    ElMessage.error(isEdit.value ? '修改失败' : '新增失败')
  } finally {
    submitLoading.value = false
  }
}

// 自动补全 URL 协议前缀
const normalizeUrl = (url) => {
  if (!url) return ''
  if (!/^https?:\/\//i.test(url)) {
    return 'http://' + url
  }
  return url
}

// 删除数据
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除「${row.title}」吗？`,
    '删除确认',
    {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await axios.delete(`${API_URL}/${row.id}`)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 对话框关闭时清空
watch(dialogVisible, (val) => {
  if (!val) {
    fileList.value = []
    readingImage.value = false
  }
})

onMounted(() => {
  fetchData()
})
</script>

<template>
  <div>
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <h2>模块数据管理</h2>
          <el-button type="primary" @click="handleAdd">+ 新增模块</el-button>
        </div>
      </template>

      <el-table :data="cards" v-loading="loading" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column label="图标" width="100" align="center">
          <template #default="scope">
            <el-image
              style="width: 40px; height: 40px"
              :src="scope.row.iconUrl"
              fit="contain"
            />
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" width="150" />
        <el-table-column prop="description" label="描述" />
        <el-table-column label="跳转目标" width="250">
          <template #default="scope">
            <el-link
              type="primary"
              :href="normalizeUrl(scope.row.targetLink)"
              target="_blank"
            >
              {{ scope.row.targetLink }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" plain @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>

        <!-- 图标上传 -->
        <el-form-item label="图标" prop="iconUrl">
          <el-upload
            v-model:file-list="fileList"
            class="upload-demo"
            action="#"
            :auto-upload="false"
            :on-change="handleChange"
            :on-remove="handleRemove"
            :on-preview="handlePreview"
            list-type="picture"
            :limit="1"
            accept="image/*"
          >
            <el-button
              type="primary"
              :disabled="fileList.length >= 1 || readingImage"
            >
              <el-icon class="el-icon--left"><Plus /></el-icon>
              {{ readingImage ? '处理中...' : '点击上传' }}
            </el-button>
            <template #tip>
              <div class="el-upload__tip">
                支持 jpg/png/webp 格式，单张图片不超过 2MB
              </div>
            </template>
          </el-upload>
        </el-form-item>

        <el-form-item label="描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入描述"
          />
        </el-form-item>
        <el-form-item label="跳转链接" prop="targetLink">
          <el-input v-model="form.targetLink" placeholder="请输入跳转链接" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button
          type="primary"
          :loading="submitLoading"
          :disabled="readingImage"
          @click="handleSubmit"
        >
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
h2 {
  margin: 0;
  color: #303133;
  font-size: 18px;
}
</style>