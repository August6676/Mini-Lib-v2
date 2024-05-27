<template>
  <div>
    <el-row>
      <el-col :span="6">
        <el-statistic title="Daily active users" style="font-size: larger" :value="users.length" />
      </el-col>
      <el-col :span="6">
        <el-statistic title="Website Visits" :value="97" />
      </el-col>
      <el-col :span="6">
        <el-statistic title="Average length of stay(ms)" :value="562">
          <template #suffix>
          </template>
        </el-statistic>
      </el-col>
      <el-col :span="6">
        <el-statistic title="Total number of likes" :value=80 />
      </el-col>
    </el-row>
  </div>
  <div class="charts-container">
    <canvas id="authorChart" class="barchart"></canvas>
    <canvas id="userProfileChart" class="piechart"></canvas>
  </div>
  <div class="main-content">
    <div class="rank-container">
      <div>
        <div class="rank-title">Top 5 Books Ranking</div>
        <div>
          <div v-show="topBooks.length > 0">
            <div v-for="(book, index) in topBooks" :key="index" class="rank-item">
              <el-row class="rank-item-title">{{ index + 1 + "      " + book.name }}</el-row>
              <el-row class="rank-item-progress">
                <el-col :span="20">
                  <el-progress :percentage="book.percent" :stroke-width="10" class="el-progress" color="skyblue"></el-progress>
                </el-col>
                <el-col :span="4">
                  <span class="rank-item-value">{{ book.value }}</span>
                </el-col>
              </el-row>
            </div>
          </div>
          <div v-show="topBooks.length === 0" class="no-data">
            暂无数据
          </div>
        </div>
      </div>
    </div>
    <div class="line-chart-container">
      <canvas id="visitLineChart" class="linechart"></canvas>
    </div>
  </div>
</template>


<script setup>
import { onMounted, ref } from 'vue';
import Chart from 'chart.js';
import axios from 'axios';

const books = ref([]);
const users = ref([]);
const userProfiles = ref([]);

// 获取所有书籍
const getAllBooks = async () => {
  try {
    const response = await axios.get('/admin/books/all');
    books.value = response.data;
    console.log('获取书籍信息成功:', books.value);
    console.log(books.value.length);
  } catch (error) {
    console.error('获取书籍信息失败:', error);
  }
};

// 获取所有用户信息
const getAllUsers = () => {
  axios.get('/admin/users')
      .then(response => {
        users.value = response.data;
        console.log('获取用户信息成功:', users.value);
        console.log(users.value.length);
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

// 创建图表
const createCharts = () => {
  // 准备用户和书籍数据的柱状图
  const authorChartCtx = document.getElementById('authorChart').getContext('2d');
  new Chart(authorChartCtx, {
    type: 'bar',
    data: {
      labels: ['Number of Books', 'Number of Users'],
      datasets: [{
        label: 'Number of Books & Users',
        data: [books.value.length, users.value.length],
        backgroundColor: 'rgba(54, 162, 235, 0.2)',
        borderColor: 'rgba(54, 162, 235, 1)',
        borderWidth: 1,
      }]
    },
    options: {
      scales: {
        yAxes: [{
          display: true,
          ticks: {
            beginAtZero: true
          }
        }]
      },
      maintainAspectRatio: false, // 允许调整图表尺寸
    }
  });

  // 确保 userProfiles.value 是一个数组
  if (Array.isArray(userProfiles.value)) {
    // 统计性别数量
    const maleCount = userProfiles.value.filter(profile => profile.gender === '男').length;
    const femaleCount = userProfiles.value.filter(profile => profile.gender === '女').length;
    console.log("男性数量:", maleCount);
    console.log("女性数量:", femaleCount);
    // 创建用户性别分布的饼状图
    const userProfileChartCtx = document.getElementById('userProfileChart').getContext('2d');
    new Chart(userProfileChartCtx, {
      type: 'pie',
      data: {
        labels: ['Male', 'Female'],
        datasets: [{
          label: 'Gender Distribution',
          data: [maleCount, femaleCount],
          backgroundColor: ['rgba(54, 162, 235, 0.2)', 'rgba(255, 99, 132, 0.2)'],
          borderColor: ['rgba(54, 162, 235, 1)', 'rgba(255, 99, 132, 1)'],
          borderWidth: 1,
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: {
            position: 'top',
          },
          title: {
            display: true,
            text: 'Gender Distribution in User Profiles'
          }
        },
        maintainAspectRatio: false, // 允许调整图表尺寸
      }
    });
  } else {
    console.error('userProfiles.value is not an array:', userProfiles.value);
  }

  // 创建折线图
  const visitLineChartCtx = document.getElementById('visitLineChart').getContext('2d');
  new Chart(visitLineChartCtx, {
    type: 'line',
    data: {
      labels: ['05-18', '05-19', '05-20', '05-21', '05-22', '05-23', '05-24'],
      datasets: [{
        label: 'Website Visits',
        data: [37, 48, 27, 44, 36, 44, 45],
        backgroundColor: 'rgba(75, 192, 192, 0.2)',
        borderColor: 'rgb(238,31,102)',
        borderWidth: 1,
        fill: false,
        tension: 0.1,
      }]
    },
    options: {
      responsive: true,
      plugins: {
        legend: {
          display: true,
          position: 'top',
        },
        title: {
          display: true,
          text: 'Website Visits Over the Past Week'
        }
      },
      scales: {
        x: {
          display: true,
        },
        y: {
          display: true,
          beginAtZero: true,
        }
      },
      maintainAspectRatio: false, // 允许调整图表尺寸
    }
  });
};

// 排行前五的书籍
const topBooks = ref([
  { name: '骆驼祥子', value: 100, percent: 100 },
  { name: '1984', value: 90, percent: 90 },
  { name: '后楼梦', value: 80, percent: 80 },
  { name: '乱世佳人', value: 70, percent: 70 },
  { name: '撒哈拉的沙漠', value: 60, percent: 60 }
]);

const fetchData = async () => {
  await Promise.all([getAllUsers(), getAllBooks(), getAllUserProfiles()]);
};

const initialize = async () => {
  try {
    await fetchData();
    createCharts();
  } catch (error) {
    console.error('Error initializing data or charts:', error);
  }
};

onMounted(initialize);


</script>

<style scoped>
.el-col {
  text-align: center;
}
.charts-container {
  margin-left: 100px;
  margin-top: 20px;
  display: flex;
  direction: initial;
  height: 300px;
  width: 400px;
}
.barchart {
  height: 200px;
  width: 100px;
}

.main-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-top: 20px;
}

.line-chart-container {
  width: 400px;
  height: 300px;
  margin-right: 160px;
}

.rank-container {
  margin-top: 50px;
  width: 382px;
  margin-left: 60px;
}
.rank-title {
  font-size: 18px;
  color: #2D8CF0;
  font-weight: bold;
  margin-top: 10px;
  margin-bottom: 10px;
  margin-left: 40px;
}
.rank-item {
  width: 350px;
  padding-left: 10px;
}
.rank-item-title {
  font-size: 14px;
  color: #606266;
}
.rank-item-progress {
  height: 18px;
}
.rank-item-value {
  color: #50c097;
  font-weight: bold;
  margin-left: -10px;
  margin-bottom: 10px;
}
.no-data {
  width: 382px;
  height: 260px;
  line-height: 260px;
  text-align: center;
  color: #909399;
}
::v-deep .el-progress__text {
  display: none;
}
::v-deep .el-progress-bar__outer {
  background-color: rgba(235,238,245,0);
}

.el-progress {
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
</style>
