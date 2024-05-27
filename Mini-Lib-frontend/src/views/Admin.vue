<template>
  <el-container class="admin-container">
    <TopNavBar />
    <el-container>
      <Sidebar />
      <el-main class="content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useStore } from "@/stores";
import { ElMessage } from "element-plus";
import router from "@/router";
import axios from "axios";
import Sidebar from '@/components/admin/SiderBar.vue';
import TopNavBar from "@/components/admin/TopNavBar.vue";

const store = useStore();

</script>

<style scoped>
.admin-container {
  display: flex;
  flex-direction: column;
  height: 100vh; /* 使整个容器占满视口高度 */
}

.content {
  flex: 1;
  padding: 20px;
}
</style>
