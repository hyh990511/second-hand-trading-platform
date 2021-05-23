"use strict";

import Vue from 'vue';
import axios from "axios";
import router from '../router'

// Full config:  https://github.com/axios/axios#request-config
// axios.defaults.baseURL = process.env.baseURL || process.env.apiUrl || '';
// axios.defaults.headers.common['Authorization'] = AUTH_TOKEN;
// axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

let config = {
  // baseURL: process.env.baseURL || process.env.apiUrl || ""
  // timeout: 60 * 1000, // Timeout
  // withCredentials: true, // Check cross-site Access-Control
};

const _axios = axios.create(config);

// _axios.interceptors.request.use(
//   function(config) {
//     // Do something before request is sent
//     console.log(config);
//     const token = localStorage.getItem('token')
//     if (token) {
//     // 将获取到的token设置给header中的Authorization
//       config.headers.Authorization = token
//     }
//     return config;
//   },
//   function(error) {
//     // Do something with request error
//     return Promise.reject(error);
//   }
// );

axios.interceptors.request.use(config => {
  // console.log("config:"+config)
  const token = localStorage.getItem('token')
  // console.log("token:"+token)
  if (token) {
// 将获取到的token设置给header中的Authorization
    config.headers.token = token
  }
  return config
})

// Add a response interceptor
// _axios.interceptors.response.use(
//   function(response) {
//     // Do something with response data
//     return response;
//   },
//   function(error) {
//     // Do something with response error
//     return Promise.reject(error);
//   }
// );

// 设置响应拦截器
axios.interceptors.response.use(res => {
// 对结果进行处理
  if (res.data.code == "404") {
// 跳转登录
    router.push('/login');
// 删除token
    localStorage.removeItem('token')
  }
  return res
}, err => {
// 如果是正常接口，会走err，err.response.status值为401 进行跳转及删除token的操作
})

Plugin.install = function(Vue, options) {
  Vue.axios = _axios;
  window.axios = _axios;
  Object.defineProperties(Vue.prototype, {
    axios: {
      get() {
        return _axios;
      }
    },
    $axios: {
      get() {
        return _axios;
      }
    },
  });
};

Vue.use(Plugin)

export default Plugin;
