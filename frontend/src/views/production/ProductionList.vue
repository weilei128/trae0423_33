<template>
  <div class="production-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>生产跟踪</span>
          <el-button type="primary" @click="handleAdd">新建生产跟踪</el-button>
        </div>
      </template>

      <div class="search-bar">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="定制需求ID">
            <el-input v-model.number="searchForm.requestId" placeholder="请输入需求ID" clearable />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
              <el-option label="待生产" :value="0" />
              <el-option label="生产中" :value="1" />
              <el-option label="已完成" :value="2" />
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
        <el-table-column prop="requestId" label="定制需求ID" />
        <el-table-column prop="processName" label="生产名称" />
        <el-table-column prop="progress" label="进度">
          <template #default="{ row }">
            <el-progress :percentage="row.progress" :show-text="true" />
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag v-if="row.status === 0">待生产</el-tag>
            <el-tag v-else-if="row.status === 1" type="info">生产中</el-tag>
            <el-tag v-else type="success">已完成</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="estimatedCompleteDate" label="预计完工日期" />
        <el-table-column prop="delayWarning" label="延期预警">
          <template #default="{ row }">
            <el-tag v-if="row.delayWarning === 1" type="danger">预警</el-tag>
            <el-tag v-else type="success">正常</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleView(row)">查看进度</el-button>
            <el-button type="info" size="small" @click="handleEdit(row)">编辑</el-button>
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
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="定制需求ID" prop="requestId">
          <el-input v-model.number="form.requestId" />
        </el-form-item>
        <el-form-item label="生产名称" prop="processName">
          <el-input v-model="form.processName" />
        </el-form-item>
        <el-form-item label="预计完工日期">
          <el-date-picker
            v-model="form.estimatedCompleteDate"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="processDialogVisible"
      title="加工进度节点"
      width="800px"
    >
      <div class="process-panel">
        <div class="process-header">
          <h3>{{ currentTracking.processName }}</h3>
          <el-progress :percentage="currentTracking.progress" :show-text="true" style="width: 200px" />
        </div>
        <div class="process-list">
          <div
            v-for="process in processes"
            :key="process.id"
            class="process-item"
            :class="{
              'status-pending': process.status === 0,
              'status-processing': process.status === 1,
              'status-completed': process.status === 2
            }"
          >
            <div class="process-step">
              <div class="step-number">{{ process.processOrder }}</div>
            </div>
            <div class="process-info">
              <div class="process-name">{{ process.processName }}</div>
              <div class="process-time">
                <span v-if="process.startTime">开始: {{ formatTime(process.startTime) }}</span>
                <span v-if="process.endTime"> / 完成: {{ formatTime(process.endTime) }}</span>
                <span v-if="process.operator"> / 操作人: {{ process.operator }}</span>
              </div>
            </div>
            <div class="process-status">
              <span v-if="process.status === 0">待开始</span>
              <span v-else-if="process.status === 1">进行中</span>
              <span v-else>已完成</span>
            </div>
            <div class="process-actions">
              <el-button
                v-if="process.status === 0"
                size="small"
                @click="handleStartProcess(process)"
              >开始</el-button>
              <el-button
                v-if="process.status === 1"
                size="small"
                type="primary"
                @click="handleCompleteProcess(process)"
              >完成</el-button>
              <el-button
                v-if="process.status === 0"
                size="small"
                type="danger"
                @click="handleDeleteProcess(process)"
              >删除</el-button>
            </div>
          </div>
        </div>
        <div class="add-process">
          <el-input v-model="newProcessName" placeholder="新增工序名称" style="width: 300px" />
          <el-button type="primary" @click="handleAddProcess">添加工序</el-button>
        </div>
      </div>
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
const processDialogVisible = ref(false)
const dialogTitle = ref('新建生产跟踪')
const formRef = ref(null)
const newProcessName = ref('')

const searchForm = reactive({
  requestId: null,
  status: null
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const form = reactive({
  id: null,
  requestId: null,
  processName: '',
  estimatedCompleteDate: '',
  remark: ''
})

const currentTracking = reactive({
  id: null,
  processName: '',
  progress: 0
})

const processes = ref([])

const rules = {
  requestId: [{ required: true, message: '请输入定制需求ID', trigger: 'blur' }],
  processName: [{ required: true, message: '请输入生产名称', trigger: 'blur' }]
}

onMounted(() => {
  loadData()
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await axios.get('/api/production', {
      params: {
        page: pagination.page,
        size: pagination.size,
        requestId: searchForm.requestId,
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

const handleSearch = () => {
  pagination.page = 1
  loadData()
}

const handleReset = () => {
  searchForm.requestId = null
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
  dialogTitle.value = '新建生产跟踪'
  Object.assign(form, {
    id: null,
    requestId: null,
    processName: '',
    estimatedCompleteDate: '',
    remark: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑生产跟踪'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    if (form.id) {
      await axios.put('/api/production', form)
      ElMessage.success('更新成功')
    } else {
      await axios.post('/api/production', form)
      ElMessage.success('新建成功')
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
  ElMessageBox.confirm('确定要删除该生产跟踪吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await axios.delete(`/api/production/${row.id}`)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

const handleView = async (row) => {
  currentTracking.id = row.id
  currentTracking.processName = row.processName
  currentTracking.progress = row.progress
  await loadProcesses(row.id)
  processDialogVisible.value = true
}

const loadProcesses = async (trackingId) => {
  try {
    const res = await axios.get(`/api/production/${trackingId}/processes`)
    if (res.data && res.data.data) {
      processes.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('加载工序失败')
  }
}

const handleStartProcess = async (process) => {
  try {
    await axios.put(`/api/production/${currentTracking.id}/processes/${process.id}/start`)
    ElMessage.success('已开始')
    await loadProcesses(currentTracking.id)
    await loadData()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleCompleteProcess = async (process) => {
  try {
    await axios.put(`/api/production/${currentTracking.id}/processes/${process.id}/complete`, null, {
      params: { operator: '管理员' }
    })
    ElMessage.success('已完成')
    await loadProcesses(currentTracking.id)
    await loadData()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleAddProcess = async () => {
  if (!newProcessName.value.trim()) {
    ElMessage.error('请输入工序名称')
    return
  }
  try {
    await axios.post(`/api/production/${currentTracking.id}/processes`, {
      processName: newProcessName.value
    })
    ElMessage.success('添加成功')
    newProcessName.value = ''
    await loadProcesses(currentTracking.id)
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleDeleteProcess = async (process) => {
  ElMessageBox.confirm('确定要删除该工序吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await axios.delete(`/api/production/${currentTracking.id}/processes/${process.id}`)
      ElMessage.success('删除成功')
      await loadProcesses(currentTracking.id)
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

const formatTime = (timeStr) => {
  if (!timeStr) return ''
  return timeStr.replace('T', ' ')
}
</script>

<style scoped>
.production-list {
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

.process-panel {
  padding: 10px;
}

.process-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.process-list {
  margin-bottom: 20px;
}

.process-item {
  display: flex;
  align-items: center;
  padding: 15px;
  margin-bottom: 10px;
  border-radius: 8px;
  background: #fafafa;
}

.status-pending {
  border-left: 4px solid #d9d9d9;
}

.status-processing {
  border-left: 4px solid #1890ff;
  background: #e6f7ff;
}

.status-completed {
  border-left: 4px solid #52c41a;
  background: #f6ffed;
}

.process-step {
  margin-right: 15px;
}

.step-number {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #fff;
  border: 2px solid #d9d9d9;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
}

.status-completed .step-number {
  background: #52c41a;
  border-color: #52c41a;
  color: #fff;
}

.status-processing .step-number {
  background: #1890ff;
  border-color: #1890ff;
  color: #fff;
}

.process-info {
  flex: 1;
}

.process-name {
  font-weight: bold;
  margin-bottom: 5px;
}

.process-time {
  font-size: 12px;
  color: #999;
}

.process-status {
  margin-right: 20px;
  font-weight: bold;
}

.status-pending .process-status {
  color: #999;
}

.status-processing .process-status {
  color: #1890ff;
}

.status-completed .process-status {
  color: #52c41a;
}

.add-process {
  display: flex;
  gap: 10px;
  padding-top: 15px;
  border-top: 1px solid #eee;
}
</style>
