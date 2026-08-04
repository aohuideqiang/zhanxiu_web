<script setup>
import { useCrud } from '../composables/useCrud'

const {
  list, loading, dialogVisible, dialogTitle, isEdit, submitLoading,
  form, formRef, handleAdd, handleEdit, handleSubmit, handleDelete
} = useCrud(
  'http://localhost:8080/api/news',
  { title: '', content: '', author: '', publishDate: '' },
  'title'
)

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  author: [{ required: true, message: '请输入作者', trigger: 'blur' }]
}
</script>

<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <h2>新闻动态管理</h2>
        <el-button type="primary" @click="handleAdd">+ 新增新闻</el-button>
      </div>
    </template>

    <el-table :data="list" v-loading="loading" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" align="center" />
      <el-table-column prop="title" label="标题" width="200" />
      <el-table-column prop="author" label="作者" width="120" align="center" />
      <el-table-column prop="publishDate" label="发布日期" width="150" align="center" />
      <el-table-column prop="content" label="内容" show-overflow-tooltip />
      <el-table-column label="操作" width="180" align="center" fixed="right">
        <template #default="scope">
          <el-button size="small" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" plain @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="作者" prop="author">
          <el-input v-model="form.author" placeholder="请输入作者" />
        </el-form-item>
        <el-form-item label="发布日期" prop="publishDate">
          <el-input v-model="form.publishDate" placeholder="如 2026-07-31" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="6" placeholder="请输入新闻内容" />
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