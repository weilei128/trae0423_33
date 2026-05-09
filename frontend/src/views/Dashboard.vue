<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon user-icon">
              <el-icon><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalCustomers }}</div>
              <div class="stat-label">客户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon goods-icon">
              <el-icon><Goods /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalProducts }}</div>
              <div class="stat-label">商品总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon order-icon">
              <el-icon><ShoppingCart /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalOrders }}</div>
              <div class="stat-label">订单总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon warning-icon">
              <el-icon><Warning /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.lowStock }}</div>
              <div class="stat-label">库存预警</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>最近客户</span>
            </div>
          </template>
          <el-table :data="recentCustomers" style="width: 100%">
            <el-table-column prop="name" label="姓名" />
            <el-table-column prop="phone" label="电话" />
            <el-table-column prop="level" label="等级">
              <template #default="{ row }">
                <el-tag v-if="row.level === 1">普通</el-tag>
                <el-tag v-else-if="row.level === 2" type="info">银卡</el-tag>
                <el-tag v-else-if="row.level === 3" type="warning">金卡</el-tag>
                <el-tag v-else type="success">钻石卡</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>快捷操作</span>
            </div>
          </template>
          <div class="quick-actions">
            <el-button type="primary" @click="$router.push('/customers')">
              <el-icon><User /></el-icon>
              新增客户
            </el-button>
            <el-button type="success" @click="$router.push('/products')">
              <el-icon><Goods /></el-icon>
              新增商品
            </el-button>
            <el-button type="warning" @click="$router.push('/orders')">
              <el-icon><ShoppingCart /></el-icon>
              新增订单
            </el-button>
            <el-button type="info" @click="$router.push('/inventory')">
              <el-icon><Box /></el-icon>
              库存管理
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const stats = ref({
  totalCustomers: 0,
  totalProducts: 0,
  totalOrders: 0,
  lowStock: 0
})

const recentCustomers = ref([])

onMounted(() => {
  loadDashboardData()
})

const loadDashboardData = async () => {
  try {
    const customerRes = await axios.get('/api/customers?size=10')
    if (customerRes.data && customerRes.data.data) {
      stats.value.totalCustomers = customerRes.data.data.total || 0
      recentCustomers.value = customerRes.data.data.records || []
    }
    
    const productRes = await axios.get('/api/products?size=1')
    if (productRes.data && productRes.data.data) {
      stats.value.totalProducts = productRes.data.data.total || 0
    }
  } catch (error) {
    console.error('加载工作台数据失败:', error)
  }
}
</script>

<style scoped>
.dashboard {
  padding: 10px;
}

.stat-card {
  margin-bottom: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: #fff;
  margin-right: 20px;
}

.user-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.goods-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.order-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.warning-icon {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #999;
  margin-top: 5px;
}

.card-header {
  font-weight: bold;
  font-size: 16px;
}

.quick-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.quick-actions .el-button {
  width: 140px;
  height: 50px;
}
</style>
