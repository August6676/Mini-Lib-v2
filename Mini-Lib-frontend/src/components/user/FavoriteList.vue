<script setup>
import {computed, ref} from 'vue';
import { useStore } from '@/stores';
import { useRouter } from 'vue-router';
import {ElMessage, ElMessageBox} from "element-plus";
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const store = useStore();
const router = useRouter();
const searchQuery = ref('');
const favoriteBooks = computed(() => {
  if (searchQuery.value) {
    return store.favorites.filter(book => book.name.includes(searchQuery.value) || book.author.includes(searchQuery.value) || book.publisher.includes(searchQuery.value));
  }
  return store.favorites;
});

const viewBook = (id) => {
  router.push({ name: 'BookDetail', params: { id } });
};

const removeBook = (id) => {
  ElMessageBox.confirm('你确定要取消收藏这本书吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    store.removeFavorite(id);
    ElMessage({
      type: 'success',
      message: '删除成功!',
    });
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '取消删除',
    });
  });
};

// const remove = (bookId) => {
//   this.favoriteBooks.splice(this.favoriteBooks.findIndex(item => item.id === bookId), 1);
// }
</script>

<template>
  <div>
    <div class="search-container">
      <img src="@/assets/image/search.png" alt="搜索图标" class="searchicon" />
      <el-input v-model="searchQuery" placeholder="搜索你的收藏夹吧！" clearable class="search-input"></el-input>
    </div>

    <el-row :gutter="60" class="book-list">
      <el-col :span="6" v-for="book in favoriteBooks" :key="book.id" class="book-item">
        <div class="book-content">
          <el-card shadow="hover" class="el-card">
            <template #header>
              <div>
                <el-text type="primary" size="large">{{ book.name }}</el-text>
              </div>
              <div>
                <el-text type="info" size="default">{{ book.author }}</el-text>
              </div>
            </template>
            <img :src="book.coverurl" alt="书籍封面" @click="viewBook(book.id)" class="book-image" style="max-width: 100%; justify-content: center;" />
            <img src="@/assets/image/trash.png" style="height: 30px; cursor: pointer" @click="removeBook(book.id)">
          </el-card>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.search-container {
  display: flex;
  justify-content: center;
  align-items: center;
  /* 上面这三个属性一起才能达到居中效果 */
  margin-bottom: 20px; /* 根据需要添加底部间距 */
}

.searchicon {
  align-items: center;
  height: 30px;
}

/* 自定义搜索输入框的样式 */
.search-input {
  text-align: center;
  align-items: center;
  width: 600px; /* 设置输入框宽度 */
  height: auto; /* 自适应高度 */
  border-radius: 4px 0 0 4px; /* 圆角，与按钮相接处为直角 */
  padding: 8px 10px; /* 内边距 */
  transition: border-color 0.3s; /* 过渡效果，当边框颜色改变时 */
}

.book-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
}
.book-item {
  display: flex;
  justify-content: center;
}
.book-content {
  width: 100%;
}
.el-card {
  width: 100%;
}
.book-image {
  cursor: pointer;
}
</style>