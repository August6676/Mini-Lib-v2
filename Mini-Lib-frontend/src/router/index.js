import { createRouter, createWebHistory } from 'vue-router'
import {useStore} from "@/stores";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL), // 路由器的工作模式
  // 配置项目的路由
  routes: [
      // 下面这些路由集合是登录注册的页面集合
    {
      path: '/', // 根路径 /，名称为 welcome
      name: 'welcome',
      component: () => import('@/views/WelcomeView.vue'), // 组件
      // 子集路由
      children: [
        {
          path: '', // 默认子路径 加载 LoginPage 组件
          name: 'welcome-login',
          component: () => import('@/components/welcome/LoginPage.vue')
        }, {
          path: 'register',
          name: 'welcome-register',
          component: () => import('@/components/welcome/RegisterPage.vue')
        }, {
          path: 'forget',
          name: 'welcome-forget',
          component: () => import('@/components/welcome/ForgetPage.vue')
        }
      ]
    }, {
      path: '/user',
      name: 'index',
      component: () => import('@/views/IndexView.vue'),
      children: [
        {
          path: '', // 默认子路径
          name: 'BookList',
          component: () => import('@/components/user/BookList.vue')
        },
        {
          path: '',
          name: 'UserCenter',
          component: () => import('@/components/user/UserCenter.vue')
        },{
          path: 'books/:id', // 每一个书籍都对应的书籍编号
          name: 'BookDetail',
            component: () => import('@/components/user/BookDetail.vue')
        },{
          path: '',
          name: 'FavoriteList',
          component: () => import('@/components/user/FavoriteList.vue')
        }
          ]
    }, {
      path: '/admin',
      name: 'admin',
      component: () => import('@/views/Admin.vue'),
      children:[
          // 在这里实现管理员的子路由 包括展示用户信息等等 可扩展
        {
          path: '',
          name: 'AdminHome',
          component: () => import('@/components/admin/AdminHome.vue')
        },
        {
          path: 'users',
          name: 'admin-users',
          component: () => import('@/components/admin/UserList.vue')
        },
        {
          path: 'books',
            name: 'admin-books',
            component: () => import('@/components/admin/BookManager.vue')
        }
      ]
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const store = useStore()
  // 用户已经登录 并且目标路由名称以 welcome- 开头，则重定向到 /index
  if(store.auth.user != null && to.name.startsWith('welcome-')) {
    next('/user')
  } else if(store.auth.user == null && (to.fullPath.startsWith('/index') || to.fullPath.startsWith('/admin'))) {
    next('/')
  } else if(to.matched.length === 0){
    next('/index')
  } else {
    next()
  }
})

export default router // 默认暴露交出去
