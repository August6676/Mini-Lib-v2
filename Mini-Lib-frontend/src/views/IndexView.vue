<template>
<!--  <div>-->
<!--      欢迎{{store.auth.user}}进入到学习平台-->
<!--  </div>-->
<!--  <div>-->
<!--      <el-button @click="logout" type="danger" plain>退出登录</el-button>-->
<!--  </div>-->
  <div>
    <el-container>
    <!-- 顶部菜单栏 -->
    <el-header class="custom-header">
      <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal">
        <el-menu-item index="logo" class="logo-item">
          <img src="/Logo.png" alt="项目Logo" class="logo"> <!-- 项目Logo -->
        </el-menu-item>
        <el-menu-item index="book-list" @click="navigateTo('BookList')">首页</el-menu-item>
        <el-menu-item index="favorites" @click="navigateTo('FavoriteList')">收藏</el-menu-item>
        <el-menu-item index="user-center" @click="navigateTo('UserCenter')">个人中心</el-menu-item>
        <el-menu-item index="logout" @click="logout">
          <img src="@/assets/image/logout.png" alt="退出登录" class="logout-icon"> <!-- 使用自定义图片 -->
        </el-menu-item>
      </el-menu>
    </el-header>
    </el-container>
    <!-- 子路由视图 -->
    <el-main>
      <router-view></router-view>
    </el-main>
  </div>

</template>

<script setup>
import {get} from "@/net";
import {ElMessage} from "element-plus";
import router from "@/router";
import {useStore} from "@/stores";
import {ref,onMounted} from 'vue'
const store = useStore() // 使用store
const  activeIndex = ref('book-list')
import {post} from "@/net";
import axios from "axios";

const navigateTo = (routeName) => {
  router.push({ name: routeName });
};
// 在组件挂载后默认导航到书籍列表
onMounted(() => {
  router.push({ name: 'BookList' });
});

const logout = async () => {
  try {
    await axios.post('/user/logout');
    store.auth.user = null;
    router.push("/");
    ElMessage.success('退出登录成功');
  } catch (error) {
    ElMessage.error('退出登录失败');
  }

};

</script>

<style scoped>
.custom-header {
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
  color: black;             /* 设置文字颜色 */
  padding: 0;
  display: inline-block;
}

.el-menu-demo {
  background: linear-gradient(0deg,#F3E7E9 27.04%,#E3EEFF 100%);
  /* safari 5.1+,chrome 10+ */
  background: -webkit-linear-gradient(0deg,#F3E7E9 27.04%,#E3EEFF 100%);
  /* ff 3.6+ */
  background: -moz-linear-gradient(0deg,#F3E7E9 27.04%,#E3EEFF 100%);
  /* opera 11.10+ */
  background: -o-linear-gradient(0deg,#F3E7E9 27.04%,#E3EEFF 100%);
  /* ie 10+ */
  background: -ms-linear-gradient(0deg,#F3E7E9 27.04%,#E3EEFF 100%);
  justify-content: flex-start; /* 将菜单项放在最右边 */
  flex: 1;

}

.el-menu-demo .el-menu-item {
  color: black; /* 设置菜单项的文字颜色 */
}

.logout-icon {
  width: 24px;  /* 设置图标的宽度 */
  height: 24px; /* 设置图标的高度 */
  cursor: pointer; /* 添加鼠标指针效果 */
}

.logo-item {
  margin-right: auto; /* 将其他菜单项推到右侧 */
}

.logo {
  height: 40px; /* 设置Logo的高度 */
}


</style>