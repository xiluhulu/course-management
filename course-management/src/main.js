import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'



import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import '@/assets/css/font/iconfont.css'

import axios from 'axios'
import * as echarts from 'echarts'



Vue.use(ElementUI);
Vue.prototype.$axios = axios;
Vue.prototype.$echarts = echarts;



axios.defaults.baseURL = 'http://localhost:8088/'

Vue.config.productionTip = false
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

axios.defaults.withCredentials =false

// 添加请求拦截器
axios.interceptors.request.use(function (config) {
  // 在发送请求之前做些什么
  let token = window.sessionStorage.getItem('token')
  config.headers['token'] = token
  return config;
}, function (error) {
  // 对请求错误做些什么
  console.log(error);
  return Promise.reject(error);
});

// 添加响应拦截器
axios.interceptors.response.use(
  function (config) {
    // 在响应之前做些什么
    // console.log(config);
    // 判断后端返回的状态是否为过期
    if(config.data.code ==405){
      ElementUI.Message.warning("token过期")
      router.push({name:'login'})
    }
    if(config.data.code==406){
      router.go(-1)
      ElementUI.Message.warning("没有权限")
      
    }
    return config;
  }, 
  function (error) {
    // 对响应错误做些什么
    console.log(error);
    return Promise.reject(error);
  }
);


