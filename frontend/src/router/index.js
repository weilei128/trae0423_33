import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../views/Layout.vue'

const routes = [
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: { title: '工作台' }
      },
      {
        path: 'customers',
        name: 'Customers',
        component: () => import('../views/customers/CustomerList.vue'),
        meta: { title: '客户管理' }
      },
      {
        path: 'products',
        name: 'Products',
        component: () => import('../views/products/ProductList.vue'),
        meta: { title: '商品展示' }
      },
      {
        path: 'customizations',
        name: 'Customizations',
        component: () => import('../views/customizations/CustomizationList.vue'),
        meta: { title: '定制需求' }
      },
      {
        path: 'production',
        name: 'Production',
        component: () => import('../views/production/ProductionList.vue'),
        meta: { title: '生产跟踪' }
      },
      {
        path: 'inventory',
        name: 'Inventory',
        component: () => import('../views/inventory/InventoryList.vue'),
        meta: { title: '库存管理' }
      },
      {
        path: 'orders',
        name: 'Orders',
        component: () => import('../views/orders/OrderList.vue'),
        meta: { title: '销售结算' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
