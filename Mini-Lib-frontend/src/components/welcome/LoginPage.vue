<template>
  <div class="el-container">
    <div style="text-align: center;margin: 0 10px ;">
      <div class="logo-container">
        <img src="@/assets/image/account.png" width="50" height="50" class="login"/>
      </div>
        <div style="margin-top: 20px">
        </div>
<!--      下面是两个文本输入框-->
      <div>
        <form><!--这里要用form表单包裹-->
        <el-input v-model="form.username" type="text" placeholder="用户名/邮箱" class="custom-input">
          <template #prefix>
            <el-icon><User /></el-icon>
          </template>
        </el-input>
        <el-input v-model="form.password" type="password"  show-password style="margin-top: 10px" placeholder="密码" class="custom-input">
          <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
        </el-input>
        </form>
      </div>
<!--上面这两个是文本输入框-->
<!-- 下面是记住我和忘记密码？-->
      <el-row style="margin-top: 5px; align-items: center;">
        <el-col :span="12" style="text-align: match-parent; height: 100%; margin-bottom: 0px">
          <el-checkbox v-model="form.remember" label="记住我"/>
        </el-col>
        <el-col :span="12" style="text-align: match-parent; height: 100%; margin-bottom: 0px">
          <el-link @click="router.push('/forget')">忘记密码？</el-link>
        </el-col>
      </el-row>
<!--立即登录按钮-->
      <div style="margin-top: 20px; display: flex; justify-content: center;">
        <el-button @click="login()" style="width: 200px; height: 40px; font-size: 16px; border-radius: 20px; margin-top: 0" type="success" plain>立即登录</el-button>
      </div>

      <el-space direction="vertical" style="margin-top: 15px; text-align: left; margin-right: 120px;color: gray">
        <el-text>
          <el-icon>
            <question-filled />
          </el-icon>
          没有账号？
        </el-text>
      </el-space>


      <div style="margin-top: 5px; display: flex; justify-content: center;">
        <el-button @click="router.push('/register')" style="width: 200px; height: 40px; font-size: 16px; border-radius: 20px;margin-bottom: 20px" type="primary" plain>注册账号</el-button>
      </div>

    </div>
  </div>
</template>

<script setup>
import {User, Lock, ElementPlus, QuestionFilled} from '@element-plus/icons-vue'
import {reactive} from "vue";
import {ElMessage} from "element-plus";
import {get, post} from "@/net";
import router from "@/router";
import {useStore} from "@/stores";

const store = useStore()

const form = reactive({
    username: '',
    password: '',
    remember: false
})

const login = () => {
  if(!form.username || !form.password) {
    ElMessage.warning('请填写用户名和密码！');
  } else {
    post('/auth/login', {
      username: form.username,
      password: form.password,
      remember: form.remember
    } , (message) => {
      ElMessage.success(message);
      // 登录成功后直接跳转到对应界面，不需要再获取用户信息
      if (form.username === 'admin') {
        // 如果是管理员，跳转到管理员界面
        console.log('admin')
        store.auth.user = form.username;
        router.push('/admin');
      } else {
        // 如果是普通用户，跳转到用户界面
        store.auth.user = form.username;
        console.log(store.auth.user)
        router.push('/user')
      }
      // store.auth.user = user;
    });
  }
};



</script>

<style scoped>
/* 自定义输入框样式 */
.custom-input  {
  margin-top: 5px;
  padding: 4px 5px; /* 调整左右内边距 */
  height: auto; /* 自动高度 */
  font-size: 12px; /* 字体大小 */
  width: 260px;
}
/* 输入框聚焦时的样式 */
.custom-input input:focus {
  border-color: #66afe9; /* 修改边框颜色 */
  box-shadow: 0 0 5px rgba(102, 175, 233, 0.6); /* 添加聚焦时的阴影效果 */
}

/* 输入框中的占位符样式 */
.custom-input input::placeholder {
  color: #bfc4cc; /* 占位符颜色 */
}

/* 输入框中的鼠标悬停样式 */
.custom-input input:hover {
  border-color: #c0c4cc; /* 修改边框颜色 */
}

/* 输入框中的错误状态样式 */
.custom-input .el-input.is-error input {
  border-color: #f56c6c; /* 错误状态下的边框颜色 */
}

.el-container  {
  margin-top: 0;
  background-color: mintcream;
  display: flex;
  border-radius: 60px; /* 圆角矩形 */
  justify-content: center;
  align-items: center;
  height: 50vh;
  background-color: rgba(255, 255, 255, 0.94); /* 半透明效果，最后一个参数是透明度，取值范围为 0（完全透明）到 1（完全不透明） */
  box-shadow: 0 0 20px rgba(1, 2, 2, 0.1); /* 添加阴影效果，最后一个参数是阴影的透明度 */
}

.logo-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 10px; /* 根据需要调整图片与页面顶部的距离 */
  margin-bottom: 0; /* 确保图片与下方内容紧贴 */
}

.login {
  height: 50px; /* 图片高度 */
  width: 50px; /* 图片宽度 */
}
</style>