import Vue from 'vue'
import App from './App.vue'

import Element from 'element-ui'

import 'element-ui/lib/theme-chalk/index.css'
import '@/assets/styles/index.scss'
Vue.config.productionTip = false
import store from './store'
Vue.use(Element)

new Vue({
  el: '#app',
  store,
  render: h => h(App)
})
