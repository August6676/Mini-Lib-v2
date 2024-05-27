import axios from 'axios';

// 创建axios实例
const instance = axios.create({
    baseURL: 'http://localhost:8080', // 替换为你的后端API的基本URL
    timeout: 5000, // 请求超时时间
});

// 请求拦截器
instance.interceptors.request.use(
    config => {
        // 在发送请求之前做些什么
        if (config.method === 'post') {
            // 如果是POST请求，检查是否包含了'username'参数，如果没有，则添加一个空字符串
            if (!config.data.hasOwnProperty('username')) {
                config.data.username = '';
            }
            // 检查是否包含了'email'参数，如果没有，则添加一个空字符串
            if (!config.data.hasOwnProperty('email')) {
                config.data.email = '';
            }
        }
        return config;
    },
    error => {
        // 对请求错误做些什么
        return Promise.reject(error);
    }
);

export default instance;
