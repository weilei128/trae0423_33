<template>
  <div class="inventory-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>库存管理</span>
          <el-button type="primary">入库登记</el-button>
        </div>
      </template>
      <el-empty description="库存管理模块开发中..." />
    </el-card>
  </div>
</template>

<script setup>
</script>

<style scoped>
.inventory-list {
  padding: 10px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
