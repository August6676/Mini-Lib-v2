<script setup lang="ts">
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage, ElDialog, ElForm, ElInput, ElButton } from 'element-plus';
import { useRouter } from 'vue-router';
import type { UploadProps } from 'element-plus';
import { Plus } from "@element-plus/icons-vue";

const user = ref({});
const isEditing = ref(false);
const newPassword = ref('');
const confirmPassword = ref('');
const oldPassword = ref('');
const showPasswordDialog = ref(false); // 控制密码修改对话框显示
const router = useRouter();
const userDetail = ref(null); // 用户详细信息

const imageUrl = ref(''); // 存储图像上传的url

// 获取用户基本信息
const fetchUserProfile = async () => {
  try {
    const res = await axios.get('/user/profile');
    if (res.data) {
      user.value = res.data;
    } else {
      ElMessage.error('用户未登录');
      router.push('/login'); // 如果未登录，跳转到登录页面
    }
  } catch (error) {
    ElMessage.error('获取用户信息失败');
    console.error(error);
    router.push('/login'); // 如果出错，跳转到登录页面
  }
};

// 获取用户详细信息
const fetchUserDetail = async () => {
  try {
    const res = await axios.get('/user/profile/details');
    if (res.data) {
      userDetail.value = res.data;
      imageUrl.value = userDetail.value.avatarUrl; // 初始加载时设置头像 URL
    } else {
      userDetail.value = {}; // 如果未获取到数据，初始化为空对象以避免空指针错误
    }
  } catch (error) {
    ElMessage.error('获取用户详细信息失败');
    console.error(error);
    userDetail.value = {}; // 出错时初始化为空对象
  }
};

// 更新用户信息
const updateProfile = async () => {
  const updatedUser = {
    ...user.value
  };
  try {
    const res = await axios.put('/user/profile', updatedUser);
    user.value = res.data;
    ElMessage.success('用户信息更新成功');
    isEditing.value = false; // 退出编辑模式
  } catch (error) {
    ElMessage.error('更新用户信息失败');
    console.error(error);
  }
};

// 更新用户详细信息
const updateProfileDetails = async () => {
  try {
    const res = await axios.put('/user/profile/details', userDetail.value);
    userDetail.value = res.data;
    ElMessage.success('用户详细信息更新成功');
  } catch (error) {
    ElMessage.error('更新用户详细信息失败');
    console.error(error);
  }
};

// 新的保存方法，调用两个更新方法
const saveAll = async () => {
  await updateProfile();
  await updateProfileDetails();
};

const enableEditing = () => {
  isEditing.value = true;
};

const cancelEditing = () => {
  isEditing.value = false;
  fetchUserProfile(); // 重新加载用户信息，以取消未保存的更改
  fetchUserDetail();  // 重新加载用户详细信息
};

// 上传图片前验证类型
const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
  if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
    ElMessage.error('头像图片必须是 JPG 或 PNG 格式!');
    return false;
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('头像图片大小不能超过 2MB!');
    return false;
  }
  return true;
};

// 处理头像上传成功
const handleAvatarSuccess: UploadProps['onSuccess'] = async (response, uploadFile) => {
  imageUrl.value = URL.createObjectURL(uploadFile.raw!);

  if (!userDetail.value) {
    ElMessage.error('用户详细信息未加载，请稍后再试');
    return;
  }

  userDetail.value.avatarUrl = response.data; // 假设后端返回的响应中包含图片URL

  try {
    const res = await axios.post(`/user/profile/avatar?avatarUrl=${encodeURIComponent(userDetail.value.avatarUrl)}`);
    if (res.status === 200) {
      ElMessage.success('头像更新成功');
      imageUrl.value = userDetail.value.avatarUrl; // 更新显示的头像URL
    }
  } catch (error) {
    ElMessage.error('更新头像失败');
    console.error(error);
  }
};

const uploadRef = ref(null);

const triggerUpload = () => {
  (uploadRef.value as any).handleClick();
};

// 验证旧密码并修改新密码
const changePassword = async () => {
  if (!oldPassword.value || !newPassword.value || !confirmPassword.value) {
    ElMessage.error('请输入旧密码、新密码和确认密码');
    return;
  }
  if (newPassword.value !== confirmPassword.value) {
    ElMessage.error('新密码和确认密码不匹配');
    return;
  }

  try {
    const res = await axios.put(`/user/validate-password?oldPassword=${encodeURIComponent(oldPassword.value)}&newPassword=${encodeURIComponent(newPassword.value)}`);
    if (res.status === 200) {
      ElMessage.success('密码修改成功');
      showPasswordDialog.value = false;
      oldPassword.value = '';
      newPassword.value = '';
      confirmPassword.value = '';
    } else {
      ElMessage.error('密码修改失败');
    }
  } catch (error) {
    ElMessage.error('验证旧密码失败');
    console.error(error);
  }
};
onMounted(() => {
  fetchUserProfile();
  fetchUserDetail();
});
</script>

<template>
  <div class="user-profile">
    <el-form :model="user" label-width="80px" class="user-form">
      <el-form-item label="头像">
        <el-upload
            ref="uploadRef"
            action="http://localhost:8080/upload"
            class="avatar-uploader"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
        >
          <el-image v-if="imageUrl" :src="imageUrl" class="avatar" @click="triggerUpload" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
      <el-form-item label="用户名">
        <el-input v-model="user.username" :disabled="!isEditing"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="user.email" :disabled="!isEditing"></el-input>
      </el-form-item>
    </el-form>
    <el-form v-if="userDetail" :model="userDetail" label-width="80px" class="user-detail-form">
      <el-form-item label="年龄">
        <el-input-number v-model="userDetail.age" :disabled="!isEditing"></el-input-number>
      </el-form-item>
      <el-form-item label="性别">
        <el-radio-group v-model="userDetail.gender" :disabled="!isEditing">
          <el-radio label="男">男</el-radio>
          <el-radio label="女">女</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="自我介绍">
        <el-input type="textarea" v-model="userDetail.bio" :disabled="!isEditing"></el-input>
      </el-form-item>
    </el-form>
    <div class="form-buttons">
      <el-button v-if="!isEditing" type="primary" @click="enableEditing">编辑信息</el-button>
      <el-button v-if="isEditing" type="primary" @click="saveAll">保存</el-button>
      <el-button v-if="isEditing" @click="cancelEditing">取消</el-button>
      <el-button type="primary" @click="showPasswordDialog = true">修改密码</el-button>
    </div>

    <!-- 密码修改对话框 -->
    <el-dialog title="修改密码" v-model="showPasswordDialog">
      <el-form>
        <el-form-item label="旧密码">
          <el-input v-model="oldPassword" type="password"></el-input>
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="newPassword" type="password"></el-input>
          </el-form-item>
          <el-form-item label="确认新密码">
            <el-input v-model="confirmPassword" type="password"></el-input>
          </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showPasswordDialog = false">取消</el-button>
        <el-button type="primary" @click="changePassword">提交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<style scoped>
.user-profile {
  padding: 20px;
  background-color: snow;
  border-radius: 8px;
}

.user-form, .user-detail-form {
  margin-bottom: 20px;
}

.form-buttons {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.avatar-uploader {
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px dashed #d9d9d9;
  border-radius: 50%;
  width: 80px;
  height: 80px;
  cursor: pointer;
}

.avatar {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}

.avatar-uploader-icon {
  font-size: 24px;
  color: #8c939d;
}
</style>
