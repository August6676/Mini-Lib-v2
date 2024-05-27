<template>
  <div>
    <div style="display: flex; margin-left: 200px">
      <img src="@/assets/image/target.gif" style="height: 35px" />
      <el-input v-model="searchQuery" placeholder="搜索用户" size="large" style="width: 500px; margin-bottom: 20px"></el-input>
    </div>

    <!-- 新增用户按钮 -->
    <el-button @click="showAddModalHandler" type="primary">新增用户</el-button>

    <!-- 删除用户按钮 -->
    <el-button @click="deleteSelectedUsers" :disabled="selectedUsers.length === 0" type="danger">删除用户</el-button>

    <!-- 用户列表表格 -->
    <el-table
        :data="filteredUsers"
        @selection-change="userListchanges"
        stripe
        border
        height="400px"
        style="width: 100%; margin-top: 20px">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID"></el-table-column>
      <el-table-column prop="username" label="用户名"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column label="操作">
        <template #default="{ row }">
          <el-button @click="editUser(row)" type="text">编辑</el-button>
          <el-button @click="viewDetail(row)" type="text">详细信息</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 编辑用户的对话框 -->
    <el-dialog v-model="showEditModal" title="编辑用户">
      <el-form :model="editedUser" label-width="80px">
        <el-form-item label="ID">
          <el-input v-model="editedUser.id" disabled></el-input>
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="editedUser.username"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="editedUser.email"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelEdit">取消</el-button>
        <el-button type="primary" @click="saveEditedUser">保存</el-button>
      </div>
    </el-dialog>

    <!-- 新增用户的对话框 -->
    <el-dialog v-model="showAddModal" title="新增用户">
      <el-form :model="newUser" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="newUser.username"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="newUser.email"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelAdd">取消</el-button>
        <el-button type="primary" @click="addUser">新增</el-button>
      </div>
    </el-dialog>

    <!-- 详细信息的对话框 -->
    <el-dialog v-model="showDetailModal" title="用户详细信息">
      <el-form :model="currentUserProfile" label-width="80px">
        <el-form-item label="ProfileID">
          <el-input v-model="currentUserProfile.id" disabled></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-input v-model="currentUserProfile.gender" disabled></el-input>
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="currentUserProfile.bio" disabled></el-input>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input v-model="currentUserProfile.age" disabled></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeDetailModal">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import {
  ElButton,
  ElTable,
  ElTableColumn,
  ElDialog,
  ElForm,
  ElFormItem,
  ElInput,
  ElMessage,
  ElMessageBox
} from 'element-plus';

const users = ref([]);
const selectedUsers = ref([]);
const searchQuery = ref('');
const showEditModal = ref(false);
const showAddModal = ref(false);
const showDetailModal = ref(false);
const editedUser = ref({ id: '', username: '', email: '' });
const currentUserProfile = ref({ id: '', userId: '', gender: '', bio: '', age: '' });
const userProfiles = ref([]);
let newUser = ref({ username: '', email: '' });

axios.defaults.withCredentials = true;

// 获取所有用户信息
const getAllUsers = () => {
  axios.get('/admin/users')
      .then(response => {
        users.value = response.data;
        console.log('获取用户信息成功:', users.value);
      })
      .catch(error => {
        console.error('获取用户信息失败:', error);
      });
};

// 获取所有用户详细信息
const getAllUserProfiles = () => {
  axios.get('/admin/userProfile')
      .then(response => {
        if (response.data.success && Array.isArray(response.data.data)) {
          userProfiles.value = response.data.data;
          console.log('获取用户详细信息成功:', userProfiles.value);
          console.log(userProfiles.value.length);
        } else {
          console.error('获取用户详细信息失败，返回的不是正确的格式:', response.data);
        }
      })
      .catch(error => {
        console.error('获取用户详细信息失败:', error);
      });
};

// 过滤用户
const filterUsers = () => {
  const query = searchQuery.value ? searchQuery.value.toLowerCase() : '';
  return users.value.filter(user => {
    const id = user.id ? user.id.toString() : '';
    const username = user.username ? user.username.toLowerCase() : '';
    const email = user.email ? user.email.toLowerCase() : '';
    return username.includes(query) || email.includes(query) || id.includes(query);
  });
};

// 删除用户
const deleteSelectedUsers = async () => {
  try {
    await ElMessageBox.confirm(
        '你确定要删除选中的用户吗？',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }
    );

    console.log('删除用户:', selectedUsers.value);
    const deletePromises = selectedUsers.value.map(async user => {
      try {
        await axios.delete(`/admin/users/${user.id}`);
        const index = users.value.findIndex(u => u.id === user.id);
        if (index !== -1) {
          users.value.splice(index, 1);
        }
      } catch (error) {
        console.error('删除用户失败:', error);
        ElMessage.error('删除用户失败');
      }
    });

    await Promise.all(deletePromises);

    selectedUsers.value = []; // 清空选中的用户
    ElMessage.success('用户删除成功');
    console.log(users.value);

  } catch (error) {
    console.log('删除操作已取消');
  }
};

const editUser = (user) => {
  editedUser.value = {...user};
  showEditModal.value = true;
};

// 修改用户
const saveEditedUser = () => {
  axios.put(`/admin/users/${editedUser.value.id}`, editedUser.value, {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  })
      .then(response => {
        const index = users.value.findIndex(user => user.id === editedUser.value.id);
        if (index !== -1) {
          users.value[index] = {...editedUser.value};
        }
        showEditModal.value = false;
      })
      .catch(error => {
        console.error('保存用户信息失败:', error);
      });
};

const cancelEdit = () => {
  showEditModal.value = false;
};

// 新增用户
const addUser = () => {
  const user = newUser.value;
  if (!user || !user.username || !user.email) {
    ElMessage.error('用户信息不完整');
    return;
  }

  axios.post('/admin/users', {
    username: user.username,
    email: user.email
  }, {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  }).then(response => {
    ElMessage.success('新增用户成功');
    console.log('新增用户成功:', response.data);
    users.value.push(response.data); // Add new user directly to users array
    showAddModal.value = false;
  }).catch(error => {
    console.error('新增用户失败:', error);
  });
};

const showAddModalHandler = () => {
  showAddModal.value = true;
};

const cancelAdd = () => {
  showAddModal.value = false;
};

const userListchanges = (val) => {
  selectedUsers.value = val;
  console.log("selectedUsers数组长度为" + selectedUsers.value.length + "选中元素为" + selectedUsers.value);
};

// 查看用户详细信息
const viewDetail = async (user) => {
  try {
    const response = await axios.get(`/admin/userProfile/${user.id}`);
    currentUserProfile.value = response.data;
    showDetailModal.value = true;
  } catch (error) {
    console.error('获取用户详细信息失败:', error);
    ElMessage.error('获取用户详细信息失败');
  }
};

const closeDetailModal = () => {
  showDetailModal.value = false;
};

onMounted(() => {
  getAllUsers();
  getAllUserProfiles();
});

const filteredUsers = computed(() => {
  return filterUsers();
});
</script>

<style scoped>
</style>
