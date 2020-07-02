import Vue from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import store from './store'
//element ui
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import 'element-theme-chalk';
//混入
import mixin from '@/libs/mixin'
//登录界面样式
import '@/styles/login.css'

//引入视频组件
import Video from 'video.js'
import 'video.js/dist/video-js.css'


import $ from 'jquery'
Vue.prototype.JQ = $


Vue.prototype.$video = Video
Vue.config.productionTip = false

Vue.use(ElementUI)

Vue.mixin(mixin)

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
