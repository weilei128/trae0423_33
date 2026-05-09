<template>
  <div class="customization-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>定制需求列表</span>
          <el-button type="primary" @click="handleAdd">新增需求</el-button>
        </div>
      </template>

      <div class="search-bar">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="需求单号">
            <el-input v-model="searchForm.requestNo" placeholder="请输入需求单号" clearable />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
              <el-option label="待接单" :value="0" />
              <el-option label="设计中" :value="1" />
              <el-option label="待确认" :value="2" />
              <el-option label="生产中" :value="3" />
              <el-option label="已完成" :value="4" />
              <el-option label="已取消" :value="5" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table :data="tableData" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="requestNo" label="需求单号" />
        <el-table-column prop="styleDesc" label="款式描述" />
        <el-table-column prop="material" label="材质" />
        <el-table-column prop="size" label="尺寸" />
        <el-table-column prop="budget" label="预算" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag v-if="row.status === 0">待接单</el-tag>
            <el-tag v-else-if="row.status === 1" type="info">设计中</el-tag>
            <el-tag v-else-if="row.status === 2" type="warning">待确认</el-tag>
            <el-tag v-else-if="row.status === 3" type="primary">生产中</el-tag>
            <el-tag v-else-if="row.status === 4" type="success">已完成</el-tag>
            <el-tag v-else type="danger">已取消</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column label="操作" width="250">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="success" size="small" v-if="row.status === 0" @click="handleAccept(row)">设计师接单</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        style="margin-top: 20px; text-align: right"
        :current-page="pagination.page"
        :page-size="pagination.size"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next"
        :page-sizes="[10, 20, 50, 100]"
        @current-change="handlePageChange"
        @size-change="handleSizeChange"
      />
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="客户ID" prop="customerId">
          <el-input v-model.number="form.customerId" />
        </el-form-item>
        <el-form-item label="款式描述" prop="styleDesc">
          <el-input v-model="form.styleDesc" type="textarea" />
        </el-form-item>
        <el-form-item label="材质">
          <el-input v-model="form.material" />
        </el-form-item>
        <el-form-item label="尺寸">
          <el-input v-model="form.size" />
        </el-form-item>
        <el-form-item label="预算">
          <el-input-number v-model="form.budget" :precision="2" />
        </el-form-item>
        <el-form-item label="详细描述">
          <el-input v-model="form.description" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="acceptDialogVisible"
      title="设计师接单"
      width="400px"
    >
      <el-form :model="acceptForm" label-width="80px">
        <el-form-item label="选择设计师">
          <el-select v-model="acceptForm.designerId">
            <el-option
              v-for="designer in designers"
              :key="designer.id"
              :label="designer.name"
              :value="designer.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="acceptDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAcceptSubmit">确定接单</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const acceptDialogVisible = ref(false)
const dialogTitle = ref('新增需求')
const formRef = ref(null)

const searchForm = reactive({
  requestNo: '',
  status: null
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const form = reactive({
  id: null,
  customerId: null,
  requestNo: '',
  styleDesc: '',
  material: '',
  size: '',
  budget: 0,
  description: '',
  referenceImages: '',
  status: 0,
  designerId: null
})

const acceptForm = reactive({
  requestId: null,
  designerId: null
})

const designers = ref([])

const rules = {
  customerId: [{ required: true, message: '请输入客户ID', trigger: 'blur' }],
  styleDesc: [{ required: true, message: '请输入款式描述', trigger: 'blur' }]
}

onMounted(() => {
  loadData()
  loadDesigners()
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await axios.get('/api/customizations', {
      params: {
        page: pagination.page,
        size: pagination.size,
        requestNo: searchForm.requestNo,
        status: searchForm.status
      }
    })
    if (res.data && res.data.data) {
      tableData.value = res.data.data.records || []
      pagination.total = res.data.data.total || 0
    }
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const loadDesigners = async () => {
  try {
    const res = await axios.get('/api/customizations/designers')
    if (res.data && res.data.data) {
      designers.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('加载设计师列表失败')
  }
}

const handleSearch = () => {
  pagination.page = 1
  loadData()
}

const handleReset = () => {
  searchForm.requestNo = ''
  searchForm.status = null
  handleSearch()
}

const handlePageChange = (page) => {
  pagination.page = page
  loadData()
}

const handleSizeChange = (size) => {
  pagination.size = size
  pagination.page = 1
  loadData()
}

const handleAdd = () => {
  dialogTitle.value = '新增需求'
  Object.assign(form, {
    id: null,
    customerId: null,
    requestNo: '',
    styleDesc: '',
    material: '',
    size: '',
    budget: 0,
    description: '',
    referenceImages: '',
    status: 0,
    designerId: null
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑需求'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    if (form.id) {
      await axios.put('/api/customizations', form)
      ElMessage.success('更新成功')
    } else {
      await axios.post('/api/customizations', form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    if (error !== false) {
      ElMessage.error('操作失败')
    }
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该需求吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await axios.delete(`/api/customizations/${row.id}`)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

const handleAccept = (row) => {
  acceptForm.requestId = row.id
  acceptForm.designerId = null
  acceptDialogVisible.value = true
}

const handleAcceptSubmit = async () => {
  if (!acceptForm.designerId) {
    ElMessage.error('请选择设计师')
    return
  }
  try {
    await axios.put(`/api/customizations/${acceptForm.requestId}/accept`, null, {
      params: { designerId: acceptForm.designerId }
    })
    ElMessage.success('接单成功')
    acceptDialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error('接单失败')
  }
}
</script>

<style scoped>
.customization-list {
  padding: 10px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-bar {
  margin-bottom: 20px;
  padding: 15px;
  background: #f9f9f9;
  border-radius: 4px;
}
</style>
