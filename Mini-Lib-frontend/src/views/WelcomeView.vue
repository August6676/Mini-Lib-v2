<template>
  <div class="background-container">
    <div class="logo-container">
      <img src="@/assets/image/Mini-Lib.png" alt="Logo" class="logo">
    </div>
    <div class="centered-component">
      <router-view v-slot="{ Component }">
        <transition name="el-fade-in-linear" mode="out-in">
          <component :is="Component" style="height: 100%;" />
        </transition>
      </router-view>
    </div>
  </div>
</template>

<style>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 70vh;
}

.centered-component {
  width: 400px;
  z-index: 1;
}
/* 背景容器样式 */
.background-container {

  background-size: cover; /* 背景图片铺满整个容器 */
  background-position: center; /* 居中显示背景图片 */
  min-height: 100vh; /* 设置容器最小高度为视窗高度 */
  display: flex; /* 使用 Flex 布局 */
  flex-direction: column; /* 使用列方向布局 */
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
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
}
.logo-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 0px; /* 根据需要调整间距 */
}
.centered-component {
  width: 400px;
  z-index: 1;
  margin-top: 20px; /* 使内容与logo保持距离 */
}

.logo {
  height: 100px; /* 根据需要调整logo大小 */
}
</style>
