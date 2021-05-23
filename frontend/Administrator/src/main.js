// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import Global from './components/Global'
import store from './store/index'
import axios from 'axios'
Vue.config.productionTip = false
Vue.use(ElementUI)
Vue.prototype.GLOBAL = Global;
/* eslint-disable no-new */
new Vue({
  el:'#app',
  router,
  store,
  render:h=>h(App)
})
//在请求头中加上token
axios.interceptors.request.use(
    config => {
      if (localStorage.getItem('token')) {
        config.headers.token = localStorage.getItem('token');
      }
      else console.log('token null!')
      return config;
    },
    error => {
      return Promise.reject(error);
    });

// 设置响应拦截器
axios.interceptors.response.use(res => {
    //返回的代码如果是401则说明token失效，直接跳转到登录界面
    if (res.data.code == "401") {
// 跳转登录
        router.push('/');
// 删除token
        localStorage.removeItem('token')
    }
    return res
}, err => {
    return Promise.reject(error);
})
