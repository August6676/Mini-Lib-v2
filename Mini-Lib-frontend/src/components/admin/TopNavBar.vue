<template>
  <el-header class="top-nav">
    <div class="nav-left">
      <img src="@/assets/image/Mini-Lib.png" style="width: 150px; margin-top: 5px;">
      <!-- 可以在这里添加其他导航内容 -->
    </div>
    <div class="nav-right">
      <img src="@/assets/image/manager.png" alt="logo" style="width: 50px; height: 50px; margin-bottom: 0px; margin-right: -10px" class="nav-logo" />
    </div>
  </el-header>
</template>

<script setup>
</script>

<style scoped>
.top-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
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
  color: #fff;
  height: 60px;
}

.nav-right {
  display: flex;
  align-items: center;
}

.nav-logo {
  height: 40px; /* 调整图片高度 */
}
</style>
