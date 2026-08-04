<script setup>
import { useCrud } from '../composables/useCrud'

const {
  list, loading, dialogVisible, dialogTitle, isEdit, submitLoading,
  form, formRef, handleAdd, handleEdit, handleSubmit, handleDelete
} = useCrud(
  'http://localhost:8080/api/products',
  { name: '', description: '', imageUrl: '', price: '' },
  'name'
)

const rules = {
  name: [{ required: true, message: '请输入产品名称', trigger: 'blur' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }]
}
</script>

<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <h2>产品展示管理</h2>
        <el-button type="primary" @click="handleAdd">+ 新增产品</el-button>
      </div>
    </template>

    <el-table :data="list" v-loading="loading" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" align="center" />
      <el-table-column label="图片" width="100" align="center">
        <template #default="scope">
          <el-image v-if="scope.row.imageUrl" style="width: 50px; height: 50px" :src="scope.row.imageUrl" fit="contain" />
        </template>
      </el-table-column>
      <el-table-column prop="name" label="产品名称" width="180" />
      <el-table-column prop="description" label="描述" show-overflow-tooltip />
      <el-table-column prop="price" label="价格" width="120" align="center" />
      <el-table-column label="操作" width="180" align="center" fixed="right">
        <template #default="scope">
          <el-button size="small" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" plain @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="产品名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入产品名称" />
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input v-model="form.price" placeholder="请输入价格" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入产品描述" />
        </el-form-item>
        <el-form-item label="图片链接" prop="imageUrl">
          <el-input v-model="form.imageUrl" placeholder="请输入图片 URL" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit(rules)">确定</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
h2 { margin: 0; color: #303133; font-size: 18px; }
</style>