import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router' // 还需要引入路由器

import 'element-plus/dist/index.css'
import axios from "axios";

import {ElementPlus} from "@element-plus/icons-vue";
import {ElButton, ElInput, ElForm, ElFormItem, ElMessage, ElMessageBox} from "element-plus";
import {ElContainer, ElHeader, ElMain, ElFooter} from "element-plus";

const app = createApp(App)
// 使用axios实现网络请求
axios.defaults.baseURL = 'http://localhost:8080'
axios.defaults.withCredentials = true; // 这个一定要带个标识 后端才不会认为session为空

// 使用路由器
app.use(createPinia())
app.use(router)

// 挂载整个应用
app.mount('#app')
