<script setup>
import {onMounted, ref, computed, reactive} from 'vue';
import { useRouter } from 'vue-router';
import { get } from '@/net';
import { ElMessage } from 'element-plus';
import axios from "axios";
import {useStore} from "@/stores";

const router = useRouter();
const books = ref([]);
const store = useStore(); //
const searchText = ref('');
const currentImageIndex = ref(0);

const handleImageClick = (index) => {
  currentImageIndex.value = index;
};

const images = ref(['http://picture.gptkong.com/20240522/16535d2b37c7334f2e9bea017333859915.png',
  'http://picture.gptkong.com/20240522/1654934c3ac73a4369ae500280d32a6ea1.png',
  'http://picture.gptkong.com/20240522/1655b2ff83f5e44d61b013501bac726c47.png',
'https://img2.baidu.com/it/u=1216019155,2595405856&fm=253&fmt=auto&app=138&f=JPEG?w=359&h=500'])

const getAllBooks = async () => {
  try {
    const res = await axios.get('/user/book/all');
    books.value = res.data;
    console.log("在getAllBooks中的方法获得了"+books.value);
  } catch (error) {
    ElMessage.error('获取书籍列表失败');
    console.log(error)
  }
};

onMounted(() => {
  getAllBooks();
});

// 过滤书籍
const filterBooks = () => {
  const query = searchText.value ? searchText.value.toLowerCase() : '';
  return books.value.filter(book => {
    const name = book.name?.toLowerCase() ?? '';
    const author = book.author?.toLowerCase() ?? '';
    const publisher = book.publisher?.toLowerCase() ?? '';
    return name.includes(query) || author.includes(query) || publisher.includes(query);
  });
};

// 跳转到书籍详情页
const viewBook = (id) => {
  router.push({ name: 'BookDetail', params: { id } });
};

// 通过计算属性获取过滤后的书籍
const filteredBooks = computed(() => filterBooks());

// 添加到收藏
const addToFavorites = (book) => {
  if (!store.isFavorite(book.id)) {
    store.addFavorite(book);
    ElMessage.success('书籍已收藏');
  } else {
    ElMessage.warning('已经收藏过啦！');
  }
};
// 跳转到Streamlit页面
const goToStreamlit = () => {
  const newWindow = window.open('', '_blank');
  if (newWindow) {
    newWindow.document.write(`
      <!DOCTYPE html>
      <html lang="en">
      <head>
        <meta charset="UTF-8">
        <link rel="icon" type="image/x-icon" href="/public/Logo.png">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>图书助理</title>
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
        <iframe src=" http://172.18.63.160:8501" frameborder="0"></iframe>
      </body>
      </html>
    `);
    newWindow.document.close();
  } else {
    ElMessage.error('无法打开新窗口，请检查浏览器的弹窗拦截设置。');
  }
};

</script>

<template>
  <div class="Mini-Lib Container">
<!--    在这里展示Mini-Lib.png-->
    <img src="@/assets/image/Mini-Lib.png" alt="Mini-Lib">
    <el-text type="info" size="large" tag="b" style="margin-top: 10px">免费的小型电子图书馆</el-text>
  </div>
  <div class="carousel">
    <el-carousel indicator-position="outside" :autoplay="true" style="margin-top: 5px;" >
      <el-carousel-item v-for="(item, index) in images" :key="index">
        <el-image class="el-image"
            style="width: 100%; height: 100%"
            :src=item
            fit="cover"
            :preview-src-list="images"
            :initial-index="currentImageIndex"
            preview-teleported
            @click="handleImageClick(index)"
        />
      </el-carousel-item>
    </el-carousel>
  </div>
  <div class="search-container">
    <img src="@/assets/image/search.png" alt="搜索图标" class="searchicon" />
    <el-input v-model="searchText" placeholder="输入你像搜索的书籍吧！" size="large" class="search-input">
    </el-input>
  </div>
  <div>
    <div class="chat" @click="goToStreamlit">
      <el-button
          circle
          class="icon-button"
      >
        <img src="@/assets/image/robot-assistant.png" alt="Chat Robot" class="robot-icon" />
      </el-button>
      <div class="chat-box"><el-text type="primary" size="large">我是您的图书助理，欢迎点击我向我提问有关于图书的问题！😉</el-text></div>
    </div>


<!--    书籍列表-->
    <el-row :gutter="60" class="book-list">
      <el-col :span="6" v-for="book in filteredBooks" :key="book.id" class="book-item">
        <div class="book-content">
          <el-card shadow="hover" class="el-card">
            <template #header>
              <div>
                <el-text type="primary" size="large">{{book.name}}</el-text>
              </div>
              <div>
                <el-text type="info" size="default">{{book.author}}</el-text>
                </div>
              </template>
            <img :src="book.coverurl" alt="书籍封面" @click="viewBook(book.id)" class="book-image" style="max-width: 100%; justify-content:center;" />
            <img :src="store.isFavorite(book.id) ? 'http://picture.gptkong.com/20240524/08591a58af9ab0430081de511a41e891b1.png' : 'http://picture.gptkong.com/20240524/09002f42994e55436fa57813d5fa9f32b3.png'" alt="收藏" @click="addToFavorites(book)" class="like" />
          </el-card>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.Mini-Lib.Container {
  flex-direction: column; /* 纵向排列子元素 */
  display:flex;
  align-items:center;
  justify-content:center;
  height: 150px;
}
.chat {
  display: flex;
  align-items: center;
  margin-top: 20px;
  margin-bottom: 20px;
}

.icon-button {
  width: 50px;
  height: 50px;
  padding: 0;
  border: cornflowerblue;
  background: none;
}

.robot-icon {
  width: 100%;
  height: 100%;
  border-radius: 100%;
  border: 3px solid powderblue;
}

.chat-box {
  margin-left: 5px;
  font-size: 16px;
  border: 1px solid lightskyblue;
  padding: 10px;
  border-radius: 20px;
}

/* 限制carousel容器的宽度和高度 */
.carousel {
  width: 100%; /* 或者设置具体的宽度，如600px */
  max-width: 10000px; /* 根据需要设置最大宽度 */
  margin: 0 auto; /* 水平居中 */
  overflow: hidden; /* 防止内容溢出 */
}

/* el-carousel样式 */
.el-carousel__container {
  height: 300px; /* 设置carousel的高度 */
  overflow: hidden; /* 确保内容在carousel内 */
}

/* el-carousel-item样式（如果需要的话） */
.el-carousel__item {
  width: 100%; /* 确保item宽度和carousel容器一致 */
  height: 100%; /* 确保item高度和carousel容器一致 */
  display: flex; /* 使用flex布局来居中图片 */
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
}

/* el-image样式 */
.el-image {
  max-width: 20%; /* 图片最大宽度为容器宽度 */
  max-height: 100%; /* 图片最大高度为容器高度 */
  object-fit: cover; /* 保持图片的宽高比，同时填充整个元素的内容框 */
}

.search-container {
  display: flex;
  justify-content: center;
  align-items: center;
  /* 上面这三个属性一起才能达到居中效果 */
  margin-bottom: 20px; /* 根据需要添加底部间距 */
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

.searchicon {
  align-items: center;
  height: 30px;
}

/* 当输入框获得焦点时，改变边框颜色 */
.search-input:focus {
  border-color: lightpink; /* Element UI 的主题色或其他你喜欢的颜色 */
  outline: none; /* 去除浏览器默认的聚焦轮廓 */
}

.el-card {
margin-bottom: 20px;
}

.book-image {
  cursor: pointer;/*鼠标悬停的时候的点击效果*/
}

.like {
  width: 30px;
  heights: 30px;
  cursor: pointer;
  margin-top: 5px;
  align-items: center;
  justify-content: center;display: flex;
}

</style>