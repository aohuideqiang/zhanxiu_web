import { ref, reactive, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

// 自动补全 URL 协议前缀
const normalizeUrl = (url) => {
  if (!url || typeof url !== 'string') return url
  if (!/^https?:\/\//i.test(url)) {
    return 'http://' + url
  }
  return url
}

/**
 * 通用 CRUD 组合式函数
 * @param {string} apiUrl - 后端接口地址，如 'http://localhost:8080/api/banners'
 * @param {object} defaultFields - 表单默认字段，如 { title: '', content: '' }
 * @param {string} nameField - 删除确认时显示名称的字段名，默认 'title'
 */
export function useCrud(apiUrl, defaultFields, nameField = 'title') {
  const list = ref([])
  const loading = ref(false)
  const dialogVisible = ref(false)
  const dialogTitle = ref('')
  const isEdit = ref(false)
  const submitLoading = ref(false)
  const form = reactive({ ...defaultFields })
  const formRef = ref()

  // 查询：获取全部数据
  const fetchData = async () => {
    loading.value = true
    try {
      const response = await axios.get(apiUrl)
      list.value = response.data
    } catch (error) {
      console.error('请求失败:', error)
      ElMessage.error('获取数据失败，请检查后端服务是否启动')
    } finally {
      loading.value = false
    }
  }

  // 重置表单为默认值
  const resetForm = () => {
    Object.keys(defaultFields).forEach(key => {
      form[key] = defaultFields[key]
    })
    form.id = null
  }

  // 新增：打开对话框
  const handleAdd = () => {
    resetForm()
    isEdit.value = false
    dialogTitle.value = '新增'
    dialogVisible.value = true
  }

  // 编辑：打开对话框并填充数据
  const handleEdit = (row) => {
    resetForm()
    isEdit.value = true
    dialogTitle.value = '编辑'
    Object.keys(defaultFields).forEach(key => {
      if (row[key] !== undefined) form[key] = row[key]
    })
    form.id = row.id
    dialogVisible.value = true
  }

  // 提交：新增或编辑，返回 true 表示成功，false 表示失败
  const handleSubmit = async (rules = {}) => {
    if (!formRef.value) return false
    submitLoading.value = true
    try {
      // 使用 Promise 方式验证表单
      await formRef.value.validate()

      // 构造提交数据（只包含表单字段，不含 id）
      const payload = { ...defaultFields }
      Object.keys(defaultFields).forEach(key => {
        let value = form[key]
        // 自动补全 URL 协议前缀（字段名以 Url 或 Link 结尾）
        if (/Url$|Link$/i.test(key)) {
          value = normalizeUrl(value)
        }
        payload[key] = value
      })

      if (isEdit.value) {
        await axios.put(`${apiUrl}/${form.id}`, payload)
        ElMessage.success('修改成功')
      } else {
        await axios.post(apiUrl, payload)
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
      fetchData()
      return true
    } catch (error) {
      // 如果是验证失败
      if (error && error.errors) {
        ElMessage.warning('请检查表单填写')
        return false
      }
      console.error('操作失败:', error)
      ElMessage.error(isEdit.value ? '修改失败' : '新增失败')
      return false
    } finally {
      submitLoading.value = false
    }
  }

  // 删除：确认后调用 DELETE 接口
  const handleDelete = (row) => {
    ElMessageBox.confirm(
      `确定要删除「${row[nameField]}」吗？`,
      '删除确认',
      { confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning' }
    ).then(async () => {
      try {
        await axios.delete(`${apiUrl}/${row.id}`)
        ElMessage.success('删除成功')
        fetchData()
      } catch (error) {
        console.error('删除失败:', error)
        ElMessage.error('删除失败')
      }
    }).catch(() => {})
  }

  onMounted(() => {
    fetchData()
  })

  return {
    list, loading, dialogVisible, dialogTitle, isEdit, submitLoading,
    form, formRef, fetchData, handleAdd, handleEdit, handleSubmit, handleDelete,
    normalizeUrl
  }
}