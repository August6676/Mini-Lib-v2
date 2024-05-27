<template>
<!--  <el-aside width="200px" class="aside" >-->
    <el-row class="tac" >
      <el-col :span="12" >
        <el-container style="height: 100%; width: 150px; margin-top: -6px">
  <el-menu
      default-active="1"
      style="margin-top: 5px; height: 100%"
      class="el-menu-vertical-demo">
    <el-menu-item index="1" @click="navigateTo('AdminHome')" style="display: flex">数据概览</el-menu-item>
    <el-menu-item index="2" @click="navigateTo('admin-users')">用户信息</el-menu-item>
    <el-menu-item index="3" @click="navigateTo('admin-books')">图书管理</el-menu-item>
    <!-- 可以在此添加更多侧边栏选项 -->
    <el-menu-item index="4" @click="logout" style="position: absolute;">
      <img src="@/assets/image/logout.png" @click="logout" style="width: 30px"/>
    </el-menu-item>
  </el-menu>
        </el-container>
      </el-col>
    </el-row>
<!--    </el-aside>-->
</template>

<script setup>
import axios from "axios";
import {ElMessage} from "element-plus";
import router from "@/router";
import { useStore } from "@/stores";
const store = useStore();
const navigateTo = (routeName) => {
  router.push({ name: routeName });
};

const logout = async () => {
  try {
    const response = await axios.post("/admin/logout");
    if (response.data.success) {
      ElMessage.success(response.data.message);
      store.auth.user = null;
      router.push({ name: 'welcome-login' }); // 确保这里是正确的路由名称
    } else {
      ElMessage.error("退出登录失败");
    }
  } catch (error) {
    console.error("退出登录失败:", error);
    ElMessage.error("退出登录失败");
  }
};
</script>

<style scoped>
.el-menu-vertical-demo {
  width: 200px;
}
.el-menu {
  /* global 94%+ browsers support */
  background: linear-gradient(0deg,#F3E7E9 27.04%,#E3EEFF 100%);
  /* safari 5.1+,chrome 10+ */
  background: -webkit-linear-gradient(0deg,#F3E7E9 27.04%,#E3EEFF 100%);
  /* ff 3.6+ */
  background: -moz-linear-gradient(0deg,#F3E7E9 27.04%,#E3EEFF 100%);
  /* opera 11.10+ */
  background: -o-linear-gradient(0deg,#F3E7E9 27.04%,#E3EEFF 100%);
  /* ie 10+ */
  background: -ms-linear-gradient(0deg,#F3E7E9 27.04%,#E3EEFF 100%);
  padding: 0;
  bottom: 0
}
.img {
  /* global 94%+ browsers support */
  background: linear-gradient(0deg,#F3E7E9 27.04%,#E3EEFF 100%);
  /* safari 5.1+,chrome 10+ */
  background: -webkit-linear-gradient(0deg,#F3E7E9 27.04%,#E3EEFF 100%);
  /* ff 3.6+ */
  background: -moz-linear-gradient(0deg,#F3E7E9 27.04%,#E3EEFF 100%);
  /* opera 11.10+ */
  background: -o-linear-gradient(0deg,#F3E7E9 27.04%,#E3EEFF 100%);
  /* ie 10+ */
  background: -ms-linear-gradient(0deg,#F3E7E9 27.04%,#E3EEFF 100%);
  padding: 0;
}
</style>
