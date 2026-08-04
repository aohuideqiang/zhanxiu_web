<script setup>
import { useCrud } from '../composables/useCrud'

const {
  list, loading, dialogVisible, dialogTitle, isEdit, submitLoading,
  form, formRef, handleAdd, handleEdit, handleSubmit, handleDelete
} = useCrud(
  'http://localhost:8080/api/contacts',
  { contactName: '', phone: '', email: '', address: '' },
  'contactName'
)

const rules = {
  contactName: [{ required: true, message: '请输入联系人', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入电话', trigger: 'blur' }]
}
</script>

<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <h2>联系我们管理</h2>
        <el-button type="primary" @click="handleAdd">+ 新增</el-button>
      </div>
    </template>

    <el-table :data="list" v-loading="loading" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" align="center" />
      <el-table-column prop="contactName" label="联系人" width="120" />
      <el-table-column prop="phone" label="电话" width="150" />
      <el-table-column prop="email" label="邮箱" width="200" />
      <el-table-column prop="address" label="地址" show-overflow-tooltip />
      <el-table-column label="操作" width="180" align="center" fixed="right">
        <template #default="scope">
          <el-button size="small" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" plain @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="联系人" prop="contactName">
          <el-input v-model="form.contactName" placeholder="请输入联系人" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入地址" />
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