import Vue from 'vue'
import App from './App'

import uView from "uview-ui";

//混入
import mixin from '@/libs/mixin.js'



Vue.use(uView);

Vue.mixin(mixin)

Vue.config.productionTip = false

App.mpType = 'app'

const app = new Vue({
	...App
})
app.$mount()
