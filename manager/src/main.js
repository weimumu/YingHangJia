import Vue from 'vue'
import App from './App'
import Vuikit from 'vuikit'
import VueResource from 'vue-resource'

Vue.use(Vuikit)
Vue.use(VueResource)


/* eslint-disable no-new */
new Vue({
  el: 'body',
  components: { App }
})
