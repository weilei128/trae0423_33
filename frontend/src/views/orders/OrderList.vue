<template>
  <div class="order-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>销售结算</span>
          <el-button type="primary">新增订单</el-button>
        </div>
      </template>
      <el-empty description="销售结算模块开发中..." />
    </el-card>
  </div>
</template>

<script setup>
</script>

<style scoped>
.order-list {
  padding: 10px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
