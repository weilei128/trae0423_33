<template>
  <el-container style="height: 100vh">
    <el-aside width="220px" style="background-color: #545c64">
      <div style="height: 60px; line-height: 60px; text-align: center; color: #fff; font-size: 18px; font-weight: bold">
        珠宝定制系统
      </div>
      <el-menu
        router
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
        default-active="$route.path"
      >
        <el-menu-item index="/dashboard">
          <el-icon><Monitor /></el-icon>
          <span>工作台</span>
        </el-menu-item>
        <el-menu-item index="/customers">
          <el-icon><User /></el-icon>
          <span>客户管理</span>
        </el-menu-item>
        <el-menu-item index="/products">
          <el-icon><Goods /></el-icon>
          <span>商品展示</span>
        </el-menu-item>
        <el-menu-item index="/customizations">
          <el-icon><Edit /></el-icon>
          <span>定制需求</span>
        </el-menu-item>
        <el-menu-item index="/production">
          <el-icon><Settings /></el-icon>
          <span>生产跟踪</span>
        </el-menu-item>
        <el-menu-item index="/inventory">
          <el-icon><Box /></el-icon>
          <span>库存管理</span>
        </el-menu-item>
        <el-menu-item index="/orders">
          <el-icon><ShoppingCart /></el-icon>
          <span>销售结算</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="background-color: #fff; border-bottom: 1px solid #eee">
        <div style="font-size: 18px; font-weight: bold; color: #333">
          {{ currentTitle }}
        </div>
      </el-header>
      <el-main style="background-color: #f5f7fa">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const currentTitle = computed(() => {
  return route.meta.title || '珠宝定制门店管理系统'
})
</script>

<style scoped>
.el-aside {
  height: 100%;
  overflow: hidden;
}
.el-menu {
  border-right: none;
}
</style>
