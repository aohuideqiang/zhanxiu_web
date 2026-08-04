<script setup>
import { ref, reactive, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Delete } from '@element-plus/icons-vue'

const API_URL = 'http://localhost:8080/api/solutions'

const loading = ref(true)
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)

const list = ref([])
const formRef = ref()

// 图片和 icon 文件列表
const imageFileList = ref([])
const iconFileList = ref([])
const readingImage = ref(false)
const readingIcon = ref(false)

// 标签输入框的内容
const tagsInput = ref('')

const form = reactive({
  id: null,
  solutionKey: '',
  title: '',
  description: '',
  tags: '',
  iconUrl: '',
  imageUrl: ''
})

// ========== 图片上传 ==========
const MAX_IMAGE_SIZE = 2 * 1024 * 1024
const handleImageChange = (uploadFile) => {
  if (!uploadFile.raw || !(uploadFile.raw instanceof File)) return
  const file = uploadFile.raw
  if (!file.type.startsWith('image/')) {
    ElMessage.error('只能上传图片文件!')
    imageFileList.value = []
    form.imageUrl = ''
    return
  }
  if (file.size > MAX_IMAGE_SIZE) {
    ElMessage.error('图片不能超过 2MB!')
    imageFileList.value = []
    form.imageUrl = ''
    return
  }
  readingImage.value = true
  const reader = new FileReader()
  reader.onload = (e) => { form.imageUrl = e.target.result; readingImage.value = false }
  reader.onerror = () => { readingImage.value = false; ElMessage.error('读取失败'); imageFileList.value = [] }
  reader.readAsDataURL(file)
}

const handleImageRemove = () => { form.imageUrl = '' }

// ========== Icon 上传 ==========
const MAX_ICON_SIZE = 2 * 1024 * 1024
const handleIconChange = (uploadFile) => {
  if (!uploadFile.raw || !(uploadFile.raw instanceof File)) return
  const file = uploadFile.raw
  if (!file.type.startsWith('image/')) {
    ElMessage.error('只能上传图片文件!')
    iconFileList.value = []
    form.iconUrl = ''
    return
  }
  if (file.size > MAX_ICON_SIZE) {
    ElMessage.error('图标不能超过 2MB!')
    iconFileList.value = []
    form.iconUrl = ''
    return
  }
  readingIcon.value = true
  const reader = new FileReader()
  reader.onload = (e) => { form.iconUrl = e.target.result; readingIcon.value = false }
  reader.onerror = () => { readingIcon.value = false; ElMessage.error('读取失败'); iconFileList.value = [] }
  reader.readAsDataURL(file)
}

const handleIconRemove = () => { form.iconUrl = '' }

// ========== 预览 ==========
const handlePreview = (file) => {
  if (file.url) window.open(file.url, '_blank')
}

// ========== 标签处理 ==========
const parseTags = (tagsStr) => {
  if (!tagsStr) return []
  return tagsStr.split(',').map(t => t.trim()).filter(Boolean)
}

const addTag = () => {
  const tag = tagsInput.value.trim()
  if (!tag) return
  const existing = parseTags(form.tags)
  if (existing.includes(tag)) {
    ElMessage.warning('标签已存在')
    return
  }
  existing.push(tag)
  form.tags = existing.join(',')
  tagsInput.value = ''
}

const removeTag = (tag) => {
  const existing = parseTags(form.tags)
  const idx = existing.indexOf(tag)
  if (idx > -1) existing.splice(idx, 1)
  form.tags = existing.join(',')
}

// ========== 加载数据 ==========
const fetchList = async () => {
  loading.value = true
  try {
    const res = await axios.get(API_URL)
    list.value = res.data
  } catch (err) {
    console.error('加载失败:', err)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// ========== 新增 ==========
const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, { id: null, solutionKey: '', title: '', description: '', tags: '', iconUrl: '', imageUrl: '' })
  imageFileList.value = []
  iconFileList.value = []
  tagsInput.value = ''
  dialogVisible.value = true
}

// ========== 编辑 ==========
const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, row)
  tagsInput.value = ''
  // 回填图片列表
  if (row.imageUrl) {
    imageFileList.value = [{ name: 'image', url: row.imageUrl }]
  } else {
    imageFileList.value = []
  }
  if (row.iconUrl) {
    iconFileList.value = [{ name: 'icon', url: row.iconUrl }]
  } else {
    iconFileList.value = []
  }
  dialogVisible.value = true
}

// ========== 提交 ==========
const handleSubmit = async () => {
  if (!form.title || !form.title.trim()) {
    ElMessage.error('请输入方案标题')
    return
  }
  if (readingImage.value || readingIcon.value) {
    ElMessage.warning('图片正在处理中，请稍候...')
    return
  }
  submitLoading.value = true
  try {
    if (isEdit.value) {
      await axios.put(`${API_URL}/${form.id}`, form)
      ElMessage.success('修改成功')
    } else {
      await axios.post(API_URL, form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    fetchList()
  } catch (err) {
    console.error('保存失败:', err)
    ElMessage.error('保存失败')
  } finally {
    submitLoading.value = false
  }
}

// ========== 删除 ==========
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除"${row.title}"吗？`, '删除确认', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await axios.delete(`${API_URL}/${row.id}`)
      ElMessage.success('删除成功')
      fetchList()
    } catch (err) {
      console.error('删除失败:', err)
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

onMounted(fetchList)
</script>

<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <h2>解决方案管理</h2>
        <el-button type="primary" @click="handleAdd">+ 新增方案</el-button>
      </div>
    </template>

    <el-table :data="list" v-loading="loading" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="60" align="center" />
      <el-table-column label="Icon" width="80" align="center">
        <template #default="scope">
          <el-image
            v-if="scope.row.iconUrl"
            :src="scope.row.iconUrl"
            :preview-src-list="[scope.row.iconUrl]"
            fit="cover"
            class="thumb"
          />
          <span v-else class="no-img">-</span>
        </template>
      </el-table-column>
      <el-table-column label="图片" width="100" align="center">
        <template #default="scope">
          <el-image
            v-if="scope.row.imageUrl"
            :src="scope.row.imageUrl"
            :preview-src-list="[scope.row.imageUrl]"
            fit="cover"
            class="thumb"
          />
          <span v-else class="no-img">-</span>
        </template>
      </el-table-column>
      <el-table-column prop="title" label="方案标题" width="160" />
      <el-table-column prop="description" label="方案描述" show-overflow-tooltip />
      <el-table-column label="标签" width="180">
        <template #default="scope">
          <el-tag
            v-for="tag in parseTags(scope.row.tags)"
            :key="tag"
            class="tag-item"
            effect="plain"
            size="small"
          >
            {{ tag }}
          </el-tag>
          <span v-if="!parseTags(scope.row.tags).length">-</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160" align="center" fixed="right">
        <template #default="scope">
          <el-button size="small" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" plain @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑方案' : '新增方案'"
      width="650px"
      :close-on-click-modal="false"
    >
      <el-form ref="formRef" :model="form" label-width="100px">
        <!-- 方案标题 -->
        <el-form-item label="方案标题" required>
          <el-input v-model="form.title" placeholder="请输入方案标题" />
        </el-form-item>

        <!-- 描述 -->
        <el-form-item label="方案描述">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入方案描述"
          />
        </el-form-item>

        <!-- Icon 上传 -->
        <el-form-item label="Icon 图标">
          <el-upload
            v-model:file-list="iconFileList"
            class="upload-demo"
            action="#"
            :auto-upload="false"
            :on-change="handleIconChange"
            :on-remove="handleIconRemove"
            :on-preview="handlePreview"
            list-type="picture"
            :limit="1"
            accept="image/*"
          >
            <el-button
              type="primary"
              :disabled="iconFileList.length >= 1 || readingIcon"
            >
              {{ readingIcon ? '处理中...' : '点击上传 Icon' }}
            </el-button>
            <template #tip>
              <div class="el-upload__tip">建议尺寸 64×64，jpg/png 不超过 2MB</div>
            </template>
          </el-upload>
        </el-form-item>

        <!-- 图片上传 -->
        <el-form-item label="方案图片">
          <el-upload
            v-model:file-list="imageFileList"
            class="upload-demo"
            action="#"
            :auto-upload="false"
            :on-change="handleImageChange"
            :on-remove="handleImageRemove"
            :on-preview="handlePreview"
            list-type="picture"
            :limit="1"
            accept="image/*"
          >
            <el-button
              type="primary"
              :disabled="imageFileList.length >= 1 || readingImage"
            >
              {{ readingImage ? '处理中...' : '点击上传图片' }}
            </el-button>
            <template #tip>
              <div class="el-upload__tip">建议尺寸 1920×1080，jpg/png 不超过 2MB</div>
            </template>
          </el-upload>
        </el-form-item>

        <!-- 标签 -->
        <el-form-item label="标签">
          <div class="tags-input-wrapper">
            <div class="tags-display">
              <el-tag
                v-for="tag in parseTags(form.tags)"
                :key="tag"
                closable
                effect="plain"
                @close="removeTag(tag)"
                class="tag-item"
              >
                {{ tag }}
              </el-tag>
              <span v-if="!parseTags(form.tags).length" class="placeholder-text">暂无标签，输入后点击添加</span>
            </div>
            <div class="tags-input-row">
              <el-input
                v-model="tagsInput"
                placeholder="输入标签后点击添加"
                @keyup.enter="addTag"
              />
              <el-button type="primary" :icon="Plus" @click="addTag">添加</el-button>
            </div>
          </div>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
h2 { margin: 0; color: #303133; font-size: 18px; }
.thumb { width: 50px; height: 50px; border-radius: 4px; }
.no-img { color: #c0c4cc; }
.tag-item { margin-right: 4px; margin-bottom: 4px; }

.tags-input-wrapper { width: 100%; }
.tags-display {
  min-height: 38px;
  padding: 4px 8px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  background: #f5f7fa;
  margin-bottom: 8px;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 4px;
}
.placeholder-text { color: #c0c4cc; font-size: 13px; }
.tags-input-row { display: flex; gap: 8px; }
</style>