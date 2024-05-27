<template>
  <div class="book-container">
    <img src="@/assets/image/back-arrow.png" @click="goBack" style="height: 40px; cursor: pointer"/>
    <div v-if="book" class="book-details">
      <h2 class="book-title">{{ book.name }}</h2>
      <img :src="book.coverurl" alt="书籍封面" class="book-image" />
      <div class="book-info">
        <p><strong>作者:</strong> {{ book.author }}</p>
        <p><strong>出版社:</strong> {{ book.publisher }}</p>
        <p><strong>出版日期:</strong> {{ formatDate(book.publishDate) }}</p>
        <p><strong>简介:</strong> {{ book.introduction }}</p>
      </div>
      <el-button type="primary" @click="openPdfInIframe(book.url)" class="view-book-button">查看图书</el-button>
      <img :src="store.isFavorite(book.id) ? 'http://picture.gptkong.com/20240524/08591a58af9ab0430081de511a41e891b1.png' : 'http://picture.gptkong.com/20240524/09002f42994e55436fa57813d5fa9f32b3.png'" alt="收藏" @click="addToFavorites(book)" class="like" />
    </div>
  </div>
</template>


<script setup>
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import axios from 'axios';
import { useStore } from '@/stores';

const route = useRoute();
const router = useRouter();
const book = ref(null);
const store = useStore();

const fetchBookDetail = async () => {
  try {
    const res = await axios.get(`/user/book/${route.params.id}`);
    book.value = res.data;
  } catch (error) {
    ElMessage.error('获取书籍详情失败');
  }
};

const addToFavorites = (book) => {
  if (!store.isFavorite(book.id)) {
    store.addFavorite(book);
    ElMessage.success('书籍已收藏');
  } else {
    ElMessage.warning('已经收藏过啦！');
  }
};

const goBack = () => {
  router.back();
};

const viewBook = () => {
  if (book.value && book.value.url) {
    window.open(book.value.url, '_blank');
  } else {
    ElMessage.error('未找到图书链接');
  }
};

const formatDate = (dateString) => {
  const options = { year: 'numeric', month: 'long', day: 'numeric' };
  return new Date(dateString).toLocaleDateString(undefined, options);
};

onMounted(() => {
  fetchBookDetail();
});

// 打开PDF的方法
const openPdfInIframe = (url) => {
  const newWindow = window.open('', '_blank');
  if (newWindow) {
    newWindow.document.write(`
      <!DOCTYPE html>
      <html lang="en">
      <head>
        <meta charset="UTF-8">
        <link rel="icon" type="image/x-icon" href="/public/Logo.png">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>PDF阅读器</title>
        <style>
          body, html {
            margin: 0;
            padding: 0;
            height: 100%;
            overflow: hidden;
          }
          iframe {
            border: none;
            width: 100%;
            height: 100%;
          }
        </style>
      </head>
      <body>
        <iframe src="${url}" frameborder="0"></iframe>
      </body>
      </html>
    `);
    newWindow.document.close();
  } else {
    ElMessage.error('无法打开新窗口，请检查浏览器的弹窗拦截设置。');
  }
};

</script>


<style scoped>
.book-container {
  max-width: 800px;
  margin: 20px auto;
  padding: 20px;
  border: 1px solid #dcdfe6;
  border-radius: 10px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  background-color: #fff;
}

.back-button {
  margin-bottom: 20px;
}

.book-details {
  text-align: center;
}

.book-title {
  margin: 10px 0;
  font-size: 24px;
  color: #303133;
}

.book-image {
  max-width: 200px;
  height: auto;
  margin: 20px 0;
  border: 1px solid #e4e7ed;
  border-radius: 10px;
}

.book-info {
  text-align: left;
  margin: 20px 0;
}

.book-info p {
  margin: 10px 0;
  font-size: 16px;
  color: #606266;
}

.view-book-button {
  margin-top: 20px;
}

.like {
  width: 30px;
  height: 30px;
  cursor: pointer;
  margin-top: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>

