<script setup>
import { ref, watch } from 'vue'
import { useCrud } from '../composables/useCrud'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const {
  list, loading, dialogVisible, dialogTitle, isEdit, submitLoading,
  form, formRef, handleAdd: crudHandleAdd, handleEdit: crudHandleEdit,
  handleSubmit, handleDelete, normalizeUrl
} = useCrud(
  'http://localhost:8080/api/banners',
  { imageUrl: '', title: '', subtitle: '', linkUrl: '', sortOrder: 0 },
  'title'
)

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  imageUrl: [{ required: true, message: '请上传轮播图片', trigger: 'change' }]
}

// ========== 图片上传相关 ==========
const fileList = ref([])
const readingImage = ref(false)  // 图片是否正在读取

// 打开新增对话框
const handleAdd = () => {
  fileList.value = []
  readingImage.value = false
  crudHandleAdd()
}

// 打开编辑对话框
const handleEdit = (row) => {
  fileList.value = row.imageUrl
    ? [{ name: 'current', url: row.imageUrl }]
    : []
  readingImage.value = false
  crudHandleEdit(row)
}

// 文件变化时：校验 + 读取为 base64
const handleChange = (uploadFile) => {
  if (!uploadFile.raw || !(uploadFile.raw instanceof File)) {
    // 可能是删除操作触发的 change，忽略
    return
  }

  const file = uploadFile.raw

  // 1. 校验文件类型
  if (!file.type.startsWith('image/')) {
    ElMessage.error('只能上传图片文件!')
    fileList.value = []
    form.imageUrl = ''
    return
  }

  // 2. 校验文件大小（2MB 以内）
  const maxSize = 2 * 1024 * 1024
  if (file.size > maxSize) {
    ElMessage.error('图片大小不能超过 2MB!')
    fileList.value = []
    form.imageUrl = ''
    return
  }

  // 3. 读取为 base64（同步等待，防止竞态）
  readingImage.value = true
  const reader = new FileReader()
  reader.onload = (e) => {
    form.imageUrl = e.target.result
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
  form.imageUrl = ''
}

// 预览图片
const handlePreview = (file) => {
  if (file.url) {
    window.open(file.url, '_blank')
  }
}

// 提交
const onSubmit = async () => {
  if (readingImage.value) {
    ElMessage.warning('图片正在处理中，请稍候...')
    return
  }
  const success = await handleSubmit(rules)
  if (success) {
    fileList.value = []
  }
}

// 对话框关闭时清空
watch(dialogVisible, (val) => {
  if (!val) {
    fileList.value = []
    readingImage.value = false
  }
})
</script>

<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <h2>首页轮播图管理</h2>
        <el-button type="primary" @click="handleAdd">+ 新增轮播</el-button>
      </div>
    </template>

    <el-table :data="list" v-loading="loading" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" align="center" />
      <el-table-column label="图片" width="120" align="center">
        <template #default="scope">
          <el-image style="width: 60px; height: 40px" :src="scope.row.imageUrl" fit="cover" />
        </template>
      </el-table-column>
      <el-table-column prop="title" label="标题" width="150" />
      <el-table-column prop="subtitle" label="副标题" />
      <el-table-column label="跳转链接" width="200" show-overflow-tooltip>
        <template #default="scope">
          <el-link type="primary" :href="normalizeUrl(scope.row.linkUrl)" target="_blank">
            {{ scope.row.linkUrl }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column prop="sortOrder" label="排序" width="80" align="center" />
      <el-table-column label="操作" width="180" align="center" fixed="right">
        <template #default="scope">
          <el-button size="small" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" plain @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="560px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="副标题" prop="subtitle">
          <el-input v-model="form.subtitle" placeholder="请输入副标题" />
        </el-form-item>

        <!-- 图片上传 -->
        <el-form-item label="轮播图片" prop="imageUrl">
          <el-upload
            v-model:file-list="fileList"
            class="upload-demo"
            action="#"
            :auto-upload="false"
            :on-change="handleChange"
            :on-remove="handleRemove"
            :on-preview="handlePreview"
            list-type="picture"handleChange
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

        <el-form-item label="跳转链接" prop="linkUrl">
          <el-input v-model="form.linkUrl" placeholder="请输入跳转链接" />
        </el-form-item>
        <el-form-item label="排序号" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button
          type="primary"
          :loading="submitLoading"
          :disabled="readingImage"
          @click="onSubmit"
        >确定</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
h2 { margin: 0; color: #303133; font-size: 18px; }
</style>