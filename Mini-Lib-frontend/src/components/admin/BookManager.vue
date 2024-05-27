<template>
  <el-container>
    <el-header style="display: flex; flex-direction: column; ">
      <!-- 搜索框 -->
      <div style="margin-left: 300px;">
        <img src="@/assets/image/search.png" style="width: 30px; margin-right: 10px;margin-bottom: -5px" alt="搜索图片">
        <el-input v-model="searchQuery" placeholder="搜索书籍" size="large" style=" width: 500px; margin-bottom: 10px;"></el-input>
      </div>
      

      <!-- 按钮部分 -->
      <div style=" gap: 10px;">
        <el-button type="primary" @click="showAddModalHandler">添加书籍</el-button>
        <el-button type="danger" @click="deleteSelectedBooks" :disabled="selectedBooks.length === 0">删除图书</el-button>
      </div>
    </el-header>

    <el-main style="margin-top: 20px">
      <el-table :data="filteredBooks"
                @selection-change="bookSelectionChanges"
                border
                stripe
                height="400px"
                style="width: 100%">
        <el-table-column  type="selection" width="55" fixed></el-table-column>
        <el-table-column  prop="id" label="id" width="55"></el-table-column>
        <el-table-column prop="name" label="书名" width="150"></el-table-column>
        <el-table-column prop="author" label="作者" width="200"></el-table-column>
        <el-table-column prop="publisher" label="出版社" width="150"></el-table-column>
        <el-table-column label="出版日期（D/M/Y）" width="160">
          <template v-slot="scope">
            <span>{{ formatDate(scope.row.publishDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="封面" width="100">
          <template v-slot="scope">
            <img :src="scope.row.coverurl" alt="cover" style="width: 50px; height: 50px;">
          </template>
        </el-table-column>
        <el-table-column label="文件地址" width="100">
          <template v-slot:default="scope">
            <el-tooltip :content="scope.row.url" placement="top">
              <el-button slot="reference" type="text">查看</el-button>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="150">
          <template v-slot="scope" style="align-items: center; display: flex;" >
            <el-button type="text" @click="showEditModalHandler(scope.row)">编辑</el-button>
            <el-button type="text" @click="openPdfInIframe(scope.row.url)">下载pdf</el-button>
          </template>
        </el-table-column>
      </el-table>

    </el-main>
  </el-container>

  <!-- 新增图书窗口 -->
  <el-dialog v-model="showAddModal" title="书籍信息">
    <el-form :model="currentBook">
      <el-form-item label="书名">
        <el-input v-model="currentBook.name"></el-input>
      </el-form-item>
      <el-form-item label="作者">
        <el-input v-model="currentBook.author"></el-input>
      </el-form-item>
      <el-form-item label="出版社">
        <el-input v-model="currentBook.publisher"></el-input>
      </el-form-item>
      <el-form-item label="介绍">
        <el-input v-model="currentBook.introduction"></el-input>
      </el-form-item>
      <el-form-item label="封面图片url">
<el-input v-model="currentBook.coverurl"></el-input>
      </el-form-item>
      <el-form-item label="书籍url">
        <el-input v-model="currentBook.url"></el-input>
      </el-form-item>
      <el-date-picker
          v-model="currentBook.publishDate"
          type="date"
          placeholder="选择出版日期"
          style="margin-bottom: 20px"
      >
      </el-date-picker>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="showAddModal = false">取消</el-button>
      <el-button type="primary" @click="addBook">保存</el-button>
    </div>
  </el-dialog>

  <!-- 编辑图书窗口 -->
  <el-dialog v-model="showEditModal" title="编辑书籍信息">
    <el-form :model="editedBook">
      <el-form-item label = "id">
        <el-input v-model="editedBook.id" disabled></el-input>
      </el-form-item>
      <el-form-item label="书名">
        <el-input v-model="editedBook.name"></el-input>
      </el-form-item>
      <el-form-item label="作者">
        <el-input v-model="editedBook.author"></el-input>
      </el-form-item>
      <el-form-item label="出版社">
        <el-input v-model="editedBook.publisher"></el-input>
      </el-form-item>
      <el-form-item label="介绍">
        <el-input v-model="editedBook.introduction"></el-input>
      </el-form-item>
      <el-form-item label="封面图片url">
        <el-input v-model="editedBook.coverurl"></el-input>
      </el-form-item>
      <el-form-item label="书籍url">
        <el-input v-model="editedBook.url"></el-input>
      </el-form-item>
      <el-date-picker
          v-model="editedBook.publishDate"
          type="date"
          placeholder="选择出版日期"
          style="margin-bottom: 10px"
      >
      </el-date-picker>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="showEditModal = false">取消</el-button>
      <el-button type="primary" @click="saveEditedBook">保存</el-button>
    </div>
  </el-dialog>
</template>

<script setup>
import {ref, reactive, onMounted, computed, watch} from 'vue';
import {ElMessageBox, ElMessage} from 'element-plus';
import axios from 'axios';

// 数据和状态
const books = ref(reactive([]));
const showEditModal = ref(false); // 针对编辑图书
const selectedBooks = ref([]);
const searchQuery = ref('');
const showAddModal = ref(false); // 针对新增图书
const currentBook = reactive({
  id: null,
  name: '',
  author: '',
  publisher: '',
  publishDate: '',
  introduction: '',
  coverurl: '',
  url: ''
});
const editedBook = reactive({
  id: null,
  name: '',
  author: '',
  publisher: '',
  publishDate: '',
  introduction: '',
  coverurl: '',
  url: ''
});

const formattedPublishDate = ref('');

// 新增图书的控制方法
const showAddModalHandler = () => {
  showAddModal.value = true;
};

// 编辑图书的控制方法
const showEditModalHandler = (book) => {
  editedBook.name = book.name;
  editedBook.id = book.id;
  editedBook.author = book.author;
  editedBook.publisher = book.publisher;
  editedBook.publishDate = book.publishDate;
  editedBook.introduction = book.introduction;
  editedBook.coverurl = book.coverurl;
  editedBook.url = book.url;
  showEditModal.value = true;
};

// 获取所有书籍
const getAllBooks = async () => {
  try {
    const response = await axios.get('/admin/books/all');
    books.value = response.data;
    // console.log(books.value)
  } catch (error) {
    console.error('获取书籍信息失败:', error);
  }
};

// 过滤书籍
const filterBooks = () => {
  const query = searchQuery.value ? searchQuery.value.toLowerCase() : '';
  return books.value.filter(book => {
    const id = book.id ? book.id.toString():'';
    const name = book.name?.toLowerCase() ?? '';
    const author = book.author?.toLowerCase() ?? '';
    const publisher = book.publisher?.toLowerCase() ?? '';
    return name.includes(query) || author.includes(query) || publisher.includes(query) || id.includes(query);
  });
};


// 通过计算属性获取过滤后的书籍
const filteredBooks = computed(() => filterBooks());

// 打开创建对话框
const openCreateDialog = () => {
  Object.assign(currentBook, {
    id: null,
    name: '',
    author: '',
    publisher: '',
    publishDate: '',
    introduction: '',
    coverurl: '',
    url: ''
  });
  formattedPublishDate.value = '';
  dialogVisible.value = true;
};



// 新增书籍 √
const addBook = () => {
  const book = currentBook;
  console.log(book)
  if (!book || !book.name || !book.author || !book.publisher || !book.publishDate || !book.introduction || !book.coverurl || !book.url) {
    ElMessage.error('书籍信息不完整');
    return;
  }

  axios.post('/admin/books', {
    name: book.name,
    author: book.author,
    publisher: book.publisher,
    publishDate: book.publishDate,
    introduction: book.introduction,
    coverurl: book.coverurl,
    url: book.url
  }, {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  }).then(response => {
    ElMessage.success('新增书籍成功');
    console.log('新增书籍成功:', response.data);
    books.value.push(response.data); // 直接将新书籍添加到书籍数组中
    showAddModal.value = false;
    // resetCurrentBook(); // 重置 currentBook 对象
  }).catch(error => {
    console.error('新增书籍失败:', error);
    ElMessage.error('新增书籍失败');
  });
  getAllBooks();
};

// 重置 currentBook 对象
const resetCurrentBook = () => {
  currentBook.name = '';
  currentBook.author = '';
  currentBook.publisher = '';
  currentBook.publishDate = '';
  currentBook.introduction = '';
  currentBook.coverurl = '';
  currentBook.url = '';
};
// 编辑书籍
const saveEditedBook = () => {
  console.log("修改的图书为"+editedBook.id)
  axios.put(`/admin/books/${editedBook.id}`, editedBook, {
    headers: {
      'Content-Type': 'application/json'
    }
  })
      .then(response => {
        const index = books.value.findIndex(book => book.id === editedBook.id);
        if (index !== -1) {
          books[index] = { ...editedBook };
        }
        ElMessage.success("书籍更新成功！")
        showEditModal.value = false;
        getAllBooks();
      })
      .catch(error => {
        console.error('保存图书信息失败:', error);
      });
};



// 删除书籍 √
const deleteSelectedBooks = async () => {
  try {
    await ElMessageBox.confirm('此操作将永久删除选中的书籍, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    });
    const book = currentBook;
    console.log("被选中的书为"+book)
    // 如果有用户确认，则进行删除操作
    const deletePromises = selectedBooks.value.map(book => {
      return axios.delete(`/admin/books/${book.id}`).then(() => {
        // 成功后，可以在这里更新书籍列表（如果需要立即更新）
        // 但通常更推荐在Promise.all之后统一更新
        // 注意：books.value和selectedBooks.value可能不是同一个数组
      }).catch(error => {
        console.error('删除图书失败:', error);
        ElMessage.error('删除失败');
        return;
      });
    });

    // 等待所有删除请求完成
    await Promise.all(deletePromises);

    // 更新书籍列表（如果books.value是全局的书籍列表）
    books.value = books.value.filter(book => !selectedBooks.value.some(selectedBook => selectedBook.id === book.id));

    // 清空选中的图书
    selectedBooks.value = [];

    // 提示用户删除完成（如果需要）
    ElMessage({
      type: 'success',
      message: '书籍删除成功'
    });
  } catch (e) {
    // 捕获取消删除或其他异常
    if (e === 'cancel') {
      // 用户取消了删除操作
      ElMessage({
        type: 'info',
        message: '已取消删除'
      });
    } else {
      // 其他异常处理
      console.error('处理删除图书时发生未知错误:', e);
    }
  }
};


// 移除封面
const removeCover = () => {
  currentBook.coverurl = '';
};

// 日期格式化函数
const formatDate = (dateString) => {
  const date = new Date(dateString);
  const day = String(date.getDate()).padStart(2, '0');
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const year = String(date.getFullYear()).slice(-2);
  return `${day}/${month}/${year}`;
};

// 监听 formattedPublishDate 的变化，同步更新 currentBook.publishDate
watch(formattedPublishDate, (newDate) => {
  const dateParts = newDate.split('/');
  if (dateParts.length === 3) {
    const formattedDate = new Date(`20${dateParts[2]}`, dateParts[1] - 1, dateParts[0]).toISOString();
    currentBook.publishDate = formattedDate;
  }
});

// 监听 currentBook.publishDate 的变化，更新 formattedPublishDate
watch(() => currentBook.publishDate, (newDate) => {
  formattedPublishDate.value = formatDate(newDate);
});

// 获取所有书籍数据
onMounted(() => {
  getAllBooks();
});

const bookSelectionChanges = (selection) => {
  selectedBooks.value = selection;
  console.log("table中方法选中的书籍列表长度为："+selectedBooks.value.length+"选中的书籍列表为："+selectedBooks.value);
};

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

<style>
.el-upload__input {
  display: none;
}
.el-link {
  margin-right: 8px;
}
</style>
